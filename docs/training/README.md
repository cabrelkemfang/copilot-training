# Training Labs — Advanced GitHub Copilot Mastery

**Project:** Employee Service (Java 21 / Spring Boot 3.x)  
**Total Duration:** 2 Days — 14 Hours

---

## Lab Index

| Lab | Primitive | Duration | Day | Module |
|-----|-----------|----------|-----|--------|
| [Lab 01](./lab-01-always-on-instructions.md) | Always-On Instructions | 45 min | Day 1 | Module 2 |
| [Lab 02](./lab-02-file-based-instructions.md) | File-Based Instructions | 45 min | Day 1 | Module 2 |
| [Lab 03](./lab-03-prompt-files.md) | Prompt Files (Slash Commands) | 60 min | Day 1 | Module 3 |
| [Lab 04](./lab-04-skills.md) | Skills | 75 min | Day 2 | Module 6 |
| [Lab 05](./lab-05-custom-agents.md) | Custom Agents | 75 min | Day 2 | Module 7 |
| [Lab 06](./lab-06-mcp.md) | Model Context Protocol (MCP) | 75 min | Day 2 | Module 8 |
| [Lab 07](./lab-07-end-to-end-workflow.md) | End-to-End Workflow (Capstone) | 90 min | Day 2 | Module 9 |

---

## Primitives Quick Reference

| Primitive | File/Location | When it activates |
|-----------|--------------|-------------------|
| Always-On Instructions | `.github/copilot-instructions.md` | Every Copilot Chat session |
| File-Based Instructions | `.github/instructions/*.instructions.md` | When `applyTo` pattern matches the active file |
| Prompt Files | `.github/prompts/*.prompt.md` | User types `/name` in chat |
| Skills | `.github/skills/*/SKILL.md` | Copilot matches user's intent to skill's `description` |
| Custom Agents | `.github/agents/*.agent.md` | User selects from agent picker (or invoked as sub-agent) |
| MCP | `.vscode/mcp.json` | Enabled in MCP panel — available as tools in any session |

---

## Files Created in This Repo

```
.github/
├── copilot-instructions.md              ← Lab 01 — Primitive 1
├── instructions/
│   └── tests.instructions.md            ← Lab 02 — Primitive 2
├── prompts/
│   ├── review-code.prompt.md            ← Lab 03 — Primitive 3
│   ├── generate-tests.prompt.md         ← Lab 03 — Primitive 3
│   └── explain-architecture.prompt.md   ← Lab 03 — Primitive 3
├── skills/
│   ├── run-and-fix-tests/
│   │   └── SKILL.md                     ← Lab 04 — Primitive 4
│   └── call-employee-api/
│       └── SKILL.md                     ← Lab 04 — Primitive 4
└── agents/
    ├── refactoring-expert.agent.md      ← Lab 05 — Primitive 5
    └── spring-migration-expert.agent.md ← Lab 05 — Primitive 5
.vscode/
└── mcp.json                             ← Lab 06 — Primitive 6
```
