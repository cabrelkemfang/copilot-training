---
name: generate-training-labs
description: "Generate a complete GitHub Copilot training lab package for a repository: creates all 6 primitive files (always-on instructions, file-based instructions, prompt files, skills, custom agents, MCP config) AND the matching hands-on lab documents (lab-0N.md). Use when preparing a Copilot training session, onboarding a new team to Copilot customization, or building a demo repo that showcases all primitives. Triggers: 'create training', 'generate labs', 'build copilot setup', 'prepare training material', 'set up primitives'."
argument-hint: "Target repo language/framework (e.g. Java Spring Boot, .NET Web API, Python FastAPI, Node Express)"
metadata:
  author: accenture-training
  version: "2.0"
---

# Generate Training Labs — GitHub Copilot Primitives

## When to Use This Skill

Use this skill when:
- You are preparing a GitHub Copilot advanced training session
- You want to generate all 6 primitive files for a new repository
- You need a hands-on lab document for each primitive
- You are onboarding a team to Copilot customization
- You want a fully working demo repository for a Copilot workshop

## Prerequisites

- The target repository must already exist with meaningful source code
- You need to understand the tech stack (language, framework, testing library)
- VS Code with GitHub Copilot Chat (Agent mode enabled)

---

## Authoritative References

Before generating any file, fetch the latest primitive specifications from these sources.
This ensures generated files always follow the most current format and best practices.

| Primitive | Reference URL |
|-----------|---------------|
| All primitives overview | https://github.com/microsoftnorman/customize-your-repo-with-github-copilot/blob/main/ReadMe.md |
| Always-On Instructions | https://github.com/microsoftnorman/customize-your-repo-with-github-copilot/blob/main/docs/primitive-1-always-on-instructions.md |
| File-Based Instructions | https://github.com/microsoftnorman/customize-your-repo-with-github-copilot/blob/main/docs/primitive-2-file-based-instructions.md |
| Prompt Files | https://github.com/microsoftnorman/customize-your-repo-with-github-copilot/blob/main/docs/primitive-3-prompts.md |
| Skills | https://github.com/microsoftnorman/customize-your-repo-with-github-copilot/blob/main/docs/primitive-4-skills.md |
| Custom Agents | https://github.com/microsoftnorman/customize-your-repo-with-github-copilot/blob/main/docs/primitive-5-custom-agents.md |
| MCP | https://github.com/microsoftnorman/customize-your-repo-with-github-copilot/blob/main/docs/primitive-6-mcp.md |
| Agent Skills spec | https://agentskills.io/spec |
| VS Code Copilot customization | https://code.visualstudio.com/docs/copilot/customization/custom-instructions |

**How to use these references:**  
Use the `fetch` tool to retrieve each page before generating the corresponding primitive file.
Synthesize the current frontmatter fields, naming rules, and best practices from the live docs.
This prevents outdated patterns from leaking into generated files.

---

## Phase 1 — Discover the Repository

Before creating anything, read the codebase.

### 1.1 Identify the tech stack

Search for build files and entry points:

| Language | Build file | Run command | Test command |
|----------|-----------|------------|-------------|
| Java (Maven) | `pom.xml` | `mvn spring-boot:run` | `mvn test` |
| Java (Gradle) | `build.gradle` | `./gradlew bootRun` | `./gradlew test` |
| .NET | `*.csproj`, `*.sln` | `dotnet run` | `dotnet test` |
| Node.js | `package.json` | `npm start` | `npm test` |
| Python | `pyproject.toml`, `requirements.txt` | `uvicorn main:app` | `pytest` |
| Go | `go.mod` | `go run .` | `go test ./...` |

Read the main build file and note:
- Language version
- Framework and version (e.g. Spring Boot 3.x, ASP.NET Core 9, FastAPI 0.100+)
- Test framework and assertion library
- Key libraries: mapping (AutoMapper, MapStruct), validation (FluentValidation, Jakarta), ORM (Entity Framework, JPA, SQLAlchemy)

### 1.2 Map the architecture

List the top-level source directories. For each layer (controller, service, repository, entity, DTO, etc.):
- Note the package/folder name
- Note the naming convention
- Identify one representative class per layer

### 1.3 Identify existing conventions

Look for:
- Existing README or ADRs (architecture decisions)
- PR templates or CONTRIBUTING.md
- Any existing `.github/` folder content
- Test class naming, assertion style, mock framework

### 1.4 Produce a discovery summary

Before creating any file, output:

```
## Discovery Summary
- Tech stack: [language+version, framework+version]
- Architecture layers: [list]
- Test framework: [library and style]
- Key conventions found: [list of 3-5]
- Existing .github content: [none / list]
- Proposed primitive theme: [brief description of what each primitive will demonstrate]
```

---

## Phase 2 — Create the Primitive Files

Create files in this exact order (each builds on the previous).

### 2.1 Primitive 1 — Always-On Instructions

**File:** `.github/copilot-instructions.md`

Required sections (adapt content to the actual stack):

```markdown
# Copilot Instructions — [Project Name]
## Project Overview          ← what the project is
## Tech Stack                ← exact libraries with versions
## Architecture & Layering   ← layer rules with ✅/❌ examples
## Coding Standards          ← naming, injection, error handling
## Security Requirements     ← PII, parameterized queries, secrets
## Testing Requirements      ← test framework, patterns, coverage
## What NOT to Do            ← explicit anti-patterns
## Why These Rules Exist     ← rationale for key decisions
```

Quality check:
- [ ] Contains ✅ correct pattern and ❌ incorrect pattern examples
- [ ] Includes rationale (why, not just what) for at least 3 rules
- [ ] Under 2000 words
- [ ] References the actual class names from the codebase

### 2.2 Primitive 2 — File-Based Instructions

**File:** `.github/instructions/tests.instructions.md`

```yaml
---
applyTo: "**/test/**"
---
```

Content must cover:
- Test framework imports and annotations
- Naming convention for test methods
- AAA pattern enforcement
- What NOT to do in tests (common mistakes)

Language-specific `applyTo` patterns:

| Language | Test pattern | Controller/Route pattern | Migration pattern |
|----------|-------------|-------------------------|-----------------|
| Java | `**/test/**` | `**/controller/**` | `**/*.sql` |
| .NET (C#) | `**/*Tests.cs`, `**/*Test.cs` | `**/Controllers/**` | `**/Migrations/**/*.cs` |
| Node.js | `**/*.test.ts`, `**/*.spec.ts` | `**/routes/**`, `**/controllers/**` | `**/migrations/**` |
| Python | `**/test_*.py`, `**/*_test.py` | `**/routers/**`, `**/views/**` | `**/migrations/**` |

Additional instruction files to consider:
- `controllers.instructions.md` — `applyTo` matching the controller folder
- `migrations.instructions.md` — `applyTo: "**/*.sql"` or migration file pattern
- `{language}.instructions.md` — for mixed-language monorepos, scope by file extension

### 2.3 Primitive 3 — Prompt Files

Create at minimum these three prompts:

| File | Mode | Variable | Purpose |
|------|------|---------|---------|
| `review-code.prompt.md` | `ask` | none | Structured code review |
| `generate-tests.prompt.md` | `agent` | `${input:className}` | Full test class scaffolding |
| `explain-architecture.prompt.md` | `ask` | `${input:scope}` | Layer/flow explanation |

Each prompt must:
- Reference the `copilot-instructions.md` conventions
- Use the actual class names and exception types from the codebase
- Have explicit output format requirements
- Specify `model` in frontmatter

### 2.4 Primitive 4 — Skills

Create at minimum these two skills:

**Skill 1 — `run-and-fix-tests`**
```
.github/skills/run-and-fix-tests/SKILL.md
```
Content: exact CLI command for the detected build tool, output parsing, failure diagnosis table, re-run step.

CLI command by stack:
- Java/Maven: `mvn test -pl {module} --no-transfer-progress`
- Java/Gradle: `./gradlew test`
- .NET: `dotnet test --no-build --verbosity normal`
- Node.js: `npm test -- --reporter=verbose`
- Python: `pytest -v --tb=short`
- Go: `go test ./... -v`

**Skill 2 — API/execution helper**  
(e.g. `call-{service}-api`, `run-{service}`, `deploy-{service}`)  
Content: how to invoke, what each endpoint/command does, error scenarios.

Description field quality check — it must include:
- What the skill does
- When to use it
- 4-6 trigger phrase keywords users would naturally say

### 2.5 Primitive 5 — Custom Agents

Create at minimum these two agents:

**Agent 1 — Refactoring/Quality specialist**  
```
.github/agents/refactoring-expert.agent.md
```

**Agent 2 — Migration/Upgrade specialist**  
```
.github/agents/{framework}-migration-expert.agent.md
```

Framework migration context by stack:
- Java Spring Boot: `javax.*` → `jakarta.*`, Spring Security 6, Flyway 9+
- .NET: .NET 6 → .NET 9, `Microsoft.Extensions.*` updates, EF Core migrations, minimal API pattern
- Node.js: Express 4 → 5, ESM imports, breaking dependency upgrades
- Python: Python 3.9 → 3.12, async patterns, Pydantic v1 → v2

Each agent MUST have all five sections:
1. Who You Are (persona + experience)
2. How You Think (methodology)
3. How You Respond (output structure)
4. What You Always Do (consistent behaviours)
5. What You Never Do (guardrails)

And at least one handoff to a follow-up action.

### 2.6 Primitive 6 — MCP Configuration

**File:** `.vscode/mcp.json`

Include these server types:
1. **Source control server** (GitHub MCP) — for issues and PRs
2. **Local API server** (fetch MCP) — for the running application
3. **Database server** (postgres/sqlite MCP) — for direct DB queries
4. **Issue tracker** (Jira/Linear MCP) — optional, with `promptString` credentials

Security rules for `mcp.json`:
- ALL tokens use `"type": "promptString"` with `"password": true`
- NO hardcoded credentials
- Connection strings reference environment variables where possible

---

## Phase 3 — Create the Lab Documents

Create one lab document per primitive under `docs/training/`.

### Lab document template

Each lab MUST follow this structure:

```markdown
# Lab 0N — Primitive N: [Name]
**Duration:** X minutes
**Module:** Day N — Module N
**Level:** Foundational | Intermediate | Advanced

## Learning Objectives   ← 4-5 bullet "By the end you will be able to..."
## Key Concept           ← explanation, comparison table, file format reference
## [Primitive] in This Project  ← what exists in THIS repo (link to actual files)
## Exercise 1            ← verify/observe (10-15 min)
## Exercise 2            ← use the primitive hands-on (15-20 min)
## Exercise 3            ← extend or create from scratch (20-30 min)
## Key Takeaways         ← table: Insight | Why It Matters
## Quick Reference       ← file tree snippet
```

### Exercise quality rules

Each exercise must:
- Give the **exact text** to type in Copilot Chat (in a code block)
- State explicitly what to **observe** ("Copilot should...")
- Include a **verification step** (expected output format or content)
- Have a clear **time estimate** in the heading

### Lab sequence and durations

| Lab | Primitive | Duration |
|-----|-----------|----------|
| Lab 01 | Always-On Instructions | 45 min |
| Lab 02 | File-Based Instructions | 45 min |
| Lab 03 | Prompt Files | 60 min |
| Lab 04 | Skills | 75 min |
| Lab 05 | Custom Agents | 75 min |
| Lab 06 | MCP | 75 min |
| Lab 07 | End-to-End Capstone | 90 min |

### Capstone lab (Lab 07) requirements

The capstone must:
- Define a single realistic feature request (user story format)
- Trace through all 6 primitives in order
- Include a "Full Primitives Map" table showing which primitive was used in which phase
- End with 5 debrief discussion questions

---

## Phase 4 — Create the Index

**File:** `docs/training/README.md`

Must contain:
- Lab index table (lab, primitive, duration, day, module)
- Primitives quick reference table (primitive, file location, when it activates)
- Complete file tree of everything created

---

## Quality Checklist

Before finishing, verify each item:

### Primitive files
- [ ] `copilot-instructions.md` — has ✅/❌ examples and rationale
- [ ] `tests.instructions.md` — `applyTo` is correct, specific to test files
- [ ] 3 prompt files — each has `name`, `description`, `agent`, `model` in frontmatter
- [ ] 2 SKILL.md files — `name` matches folder name, description has trigger keywords
- [ ] 2 agent files — have all 5 sections, at least 1 handoff each
- [ ] `mcp.json` — no hardcoded secrets, uses `promptString`

### Lab documents
- [ ] 7 lab files created (`lab-01` through `lab-07`)
- [ ] Each lab references actual file paths in the repo (not generic names)
- [ ] Each exercise has exact prompt text to type
- [ ] Lab 07 has the full primitives map table
- [ ] `README.md` index is complete

---

## Common Patterns Table

| Goal | Pattern |
|------|---------|
| Show a rule was learned from a real bug | "In Q1 2025, we had 3 production bugs because X. That's why we require Y." |
| Make an exercise fail-safe | Add "If Copilot doesn't do X, check that Y is enabled" |
| Strong skill description | "[Verb] [what] using [tool]. Use when [trigger phrases]." |
| Agent persona specificity | "20 years experience", "guided 30+ projects", "you've seen what works" |
| Exercise verification | Bullet list of exactly what should appear in Copilot's response |

## Language-Specific Guidance

### .NET (C# / ASP.NET Core)
- Architecture layers: `Controllers/` → `Services/` → `Repositories/` → `Entities/` → `DTOs/`
- Mapping: AutoMapper or Mapster (not manual mapping in services)
- Validation: FluentValidation or DataAnnotations at the controller boundary
- Exceptions: custom exception middleware (`UseExceptionHandler`) — never catch in controllers
- Test framework: xUnit + FluentAssertions + Moq/NSubstitute
- Test naming: `MethodName_StateUnderTest_ExpectedBehaviour`
- `applyTo` for tests: `**/*Tests.cs`
- Migration file pattern: `**/Migrations/**/*.cs`
- Run command: `dotnet test --no-build --verbosity normal`

### Java (Spring Boot)
- Test framework: JUnit 5 + AssertJ + Mockito
- Test naming: `should_[expected]_when_[condition]`
- Run command: `mvn test -pl {module} --no-transfer-progress`

### Python (FastAPI / Django)
- Test framework: pytest + httpx (async) or Django test client
- Test naming: `test_{action}_{condition}_{expectation}`
- Run command: `pytest -v --tb=short`

### Node.js (Express / NestJS)
- Test framework: Jest + Supertest
- Test naming: `should {expectation} when {condition}`
- Run command: `npm test -- --forceExit`

---

## Edge Cases

- **Monorepo:** create one `copilot-instructions.md` at root + subfolder `AGENTS.md` for each module
- **No REST API:** replace `call-{service}-api` skill with a `run-{service}` CLI skill
- **No database:** replace the DB MCP with a filesystem or search MCP
- **Multiple languages:** create one file-based instruction per language (`applyTo: "**/*.py"`, `applyTo: "**/*.cs"` etc.)
- **.NET + EF Core:** the migration skill should use `dotnet ef migrations add {name}` and `dotnet ef database update`
- **Minimal API (.NET):** no Controller layer — adapt the architecture rules to describe endpoint groups and `IEndpointRouteBuilder` extension methods
- **References outdated:** if fetched pages return 404, fall back to the overview at https://github.com/microsoftnorman/customize-your-repo-with-github-copilot/blob/main/ReadMe.md
