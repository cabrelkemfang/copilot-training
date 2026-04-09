# Lab 06 — Primitive 6: Model Context Protocol (MCP)

**Duration:** 75 minutes  
**Module:** Day 2 — Module 8  
**Level:** Advanced

---

## Learning Objectives

By the end of this lab, you will be able to:
- Explain what MCP is and why the local codebase is not enough context
- Read and understand a `.vscode/mcp.json` configuration file
- Connect Copilot to a live local API, a PostgreSQL database, and GitHub
- Use Copilot to query external systems in natural language
- Know when to use MCP vs. a Skill for external integrations

---

## Key Concept

> **MCP (Model Context Protocol)** is an open standard that connects Copilot to external systems — databases, APIs, issue trackers, CI/CD tools. Without MCP, Copilot only knows what is in your open files. With MCP, it can query live data, create tickets, push code, and more.

### Why MCP matters

```
WITHOUT MCP                          WITH MCP
──────────────────────────────────   ──────────────────────────────────
Copilot knows:                       Copilot also knows:
 - Your open files                    - Current database state
 - Workspace structure                - Live API responses
 - Instructions/Skills/Agents         - GitHub issues and PRs
                                      - Jira tickets
                                      - External documentation
```

### The relationship between MCP and Skills

| | MCP Server | Skill |
|-|-----------|-------|
| Provides | Access to an external system | Knowledge of how to use it |
| Handles | Authentication, API calls | Templates, conventions, workflows |
| Example | Jira MCP — connects to Jira API | Jira Skill — knows your ticket format |
| Best combined? | Yes — MCP + Skill = access + knowledge |

---

## MCP Configuration in This Project

File: [`.vscode/mcp.json`](../../.vscode/mcp.json)

```json
{
  "servers": {
    "github":         { ... },   // GitHub API — issues, PRs, code
    "employee-api":   { ... },   // Local Employee REST API (fetch)
    "employee-database": { ... }, // PostgreSQL — direct DB access
    "jira":           { ... }    // Jira — ticket management
  }
}
```

### Security model

- Credentials are stored via `"type": "promptString"` with `"password": true`
- VS Code prompts once and securely stores the value
- **Never** hardcode tokens in `mcp.json` — the file is committed to Git

---

## Exercise 1 — Connect to the Local Employee API (20 min)

This exercise uses the `employee-api` MCP server, which wraps the running application with the `fetch` MCP server.

### Step 1 — Start the application

```bash
mvn spring-boot:run -pl employee-launcher
```

Wait for: `Started EmployeeApplication in X.XXX seconds`

### Step 2 — Enable the MCP server

In VS Code, open the MCP panel (from the Copilot Chat gear → MCP Servers).  
Enable `employee-api`.

### Step 3 — Query through Copilot

In Copilot Chat:
```
List all the employees in the system
```

**Copilot will:**
1. Call `GET http://localhost:8080/api/employees`
2. Parse the JSON response
3. Display the employees in a readable format

Then:
```
Create an employee named Paul Durand with email paul.durand@accenture.com and job title ARCHITECT
```

**Copilot will:**
1. Build the JSON request body
2. Call `POST http://localhost:8080/api/employees`
3. Show the `201 Created` response with the new employee's ID

### Step 4 — Test error scenarios

```
Try to create another employee with the same email paul.durand@accenture.com
```

**Copilot should:**
1. Call the POST endpoint
2. Receive a `409 Conflict`
3. Explain that the email is already in use

---

## Exercise 2 — Query the Database Directly (20 min)

This exercise uses the `employee-database` MCP server (PostgreSQL).

> **Prerequisite:** PostgreSQL running locally with the `employee_db` database. See `application-local.yaml` for credentials.

### Enable the MCP server

Open `.vscode/mcp.json` and verify the connection string:
```json
"employee-database": {
  "command": "npx",
  "args": [
    "-y",
    "@modelcontextprotocol/server-postgres",
    "postgresql://employee_user:employee_pass@localhost:5432/employee_db"
  ]
}
```

### Query in natural language

```
Show me all employees whose email domain is not accenture.com
```

```
How many employees are in each job title category?
```

```
Show me the 5 most recently created employees
```

**Observe:** Copilot translates natural language → SQL → executes → formats results.

### Audit query

```
Show me all Flyway migration checksums and versions that have been applied
```

This queries the `flyway_schema_history` table. Useful for understanding the current DB state.

---

## Exercise 3 — Connect to GitHub (15 min)

The `github` MCP server uses the official GitHub MCP endpoint.

### Setup

In Copilot Chat, the server will prompt for your GitHub token (once, securely stored).

### Use natural language to interact with GitHub

```
Create a GitHub issue: "Missing input validation on DELETE /api/employees/{id}"
with label "bug" and description explaining that the path variable is not validated for minimum value.
```

```
Show me the open issues in this repository
```

```
What are the last 3 commits on the main branch?
```

**Observe:** Copilot makes real API calls to GitHub. The issue is created in the actual repository.

---

## Exercise 4 — Combine MCP with a Skill (20 min)

This demonstrates the **MCP + Skill hybrid pattern**.

### Scenario

You want to create a Jira ticket for a real bug you found during the code review in Lab 03.

### Step 1 — Enable Jira MCP

Configure the Jira MCP server in `.vscode/mcp.json` with your credentials (the server prompts for the API token).

### Step 2 — Ask naturally

```
Create a Jira bug ticket for the issue we found: the EmployeeController 
is missing @Valid annotation on the update endpoint, which allows 
invalid data to pass through to the service layer.
Priority: High. Project: EMPLOYEE.
```

**What happens:**
1. The `call-employee-api` skill is NOT used (this is a ticket, not an API call — Copilot distinguishes intent)
2. The Jira MCP server calls `POST /rest/api/3/issue`
3. Copilot formats the description with reproduction steps
4. Returns the Jira issue key (e.g. `EMPLOYEE-42`) and URL

### Step 3 — Link to the code

```
Add a code comment above the update method in EmployeeController referencing EMPLOYEE-42
```

Copilot now connects the Jira ticket back to the source code.

---

## Key Takeaways

| Insight | Why It Matters |
|---------|---------------|
| MCP breaks the "local files only" limitation | Copilot can act on live systems |
| Skills + MCP = access + knowledge | MCP handles auth; skills encode conventions |
| Never hardcode tokens in `mcp.json` | The file is public; use `promptString` with `password: true` |
| MCP is vendor-neutral (open standard) | Same protocol works with any AI agent |
| Start minimal | Connect one server, master it, then add more |

---

## MCP Decision Guide

| Capability needed | Solution |
|-------------------|---------|
| Query live database | `@modelcontextprotocol/server-postgres` |
| Call local REST API | `@modelcontextprotocol/server-fetch` |
| GitHub issues / PRs | GitHub MCP server (`api.githubcopilot.com/mcp/`) |
| Jira tickets | `@anthropic-ai/mcp-server-jira` |
| Confluence docs | Atlassian MCP server |
| Browse web documentation | `@modelcontextprotocol/server-fetch` |

---

## Security Checklist for MCP

Before committing `mcp.json`:

- [ ] All tokens use `"type": "promptString"` with `"password": true`
- [ ] No hardcoded passwords or API keys in the file
- [ ] Database connection strings don't contain production credentials
- [ ] MCP servers are scoped to the minimum required permissions
- [ ] `.vscode/mcp.json` is reviewed in PR just like any other config file

---

**Previous:** [Lab 05 — Custom Agents](./lab-05-custom-agents.md)  
**Next:** [Lab 07 — End-to-End Workflow](./lab-07-end-to-end-workflow.md)
