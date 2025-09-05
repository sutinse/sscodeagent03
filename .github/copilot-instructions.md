# sscodeagent03 Repository Instructions

**ALWAYS follow these instructions first and only fallback to additional search and context gathering if the information in these instructions is incomplete or found to be in error.**

## Repository Overview
This is a minimal repository currently containing only a README.md file. The repository is in its initial state and does not yet contain any application code, build systems, or test frameworks.

## Environment Setup
The development environment includes:
- Git 2.51.0 (installed at `/usr/bin/git`)
- Node.js v20.19.4 (installed at `/usr/local/bin/node`)
- npm 10.8.2 (installed at `/usr/local/bin/npm`)
- Python 3.12.3 (installed at `/usr/bin/python3`)

## Working Effectively

### Repository Structure
```
/home/runner/work/sscodeagent03/sscodeagent03/
├── .git/                   # Git repository metadata
├── .github/               # GitHub configuration directory
│   └── copilot-instructions.md  # This file
└── README.md              # Basic repository description
```

### Basic Operations
- **Repository root**: Always work from `/home/runner/work/sscodeagent03/sscodeagent03/`
- **Check status**: `git --no-pager status` (takes <0.01 seconds)
- **View history**: `git --no-pager log --oneline -10` (takes <0.01 seconds)
- **List files**: `ls -la` or `find . -type f | sort`

### Current Limitations
- **NO BUILD SYSTEM**: There is no package.json, Makefile, or other build configuration
- **NO TESTS**: No test frameworks or test files exist
- **NO DEPENDENCIES**: No package managers are configured
- **NO APPLICATION CODE**: Repository contains only documentation
- **NO CI/CD**: No GitHub Actions workflows exist yet

## Development Workflow

### Making Changes
1. **ALWAYS** check current status: `git --no-pager status`
2. Make your changes using appropriate tools
3. **ALWAYS** validate changes: `git --no-pager diff`
4. Stage changes: `git add .`
5. Commit changes: `git commit -m "descriptive message"`
6. Push changes: `git push origin [branch-name]`

### Adding New Project Types
If you need to add a new application or framework:

#### For Node.js Projects
1. Initialize: `npm init -y`
2. Install dependencies: `npm install [packages]`
3. NEVER CANCEL: npm install typically takes 1-5 minutes depending on dependencies
4. Add scripts to package.json as needed
5. **ALWAYS** test: `npm run test` (if tests are added)

#### For Python Projects
1. Create requirements.txt or pyproject.toml
2. Set up virtual environment: `python3 -m venv venv`
3. Activate: `source venv/bin/activate`
4. Install dependencies: `pip install -r requirements.txt`
5. **ALWAYS** test: `python -m pytest` (if tests are added)

#### For any project type
1. **ALWAYS** add appropriate .gitignore file
2. **ALWAYS** update this instructions file with new build/test commands
3. **ALWAYS** document exact commands and expected timing

## Validation Requirements

### Before Committing Changes
- **ALWAYS** run `git --no-pager status` to verify staged changes
- **ALWAYS** run `git --no-pager diff --cached` to review what will be committed
- **NEVER** commit files that should be in .gitignore (node_modules, build artifacts, etc.)

### When Adding Build Systems
- **ALWAYS** test every command before documenting it
- **ALWAYS** measure actual build times and add 50% buffer for timeouts
- **NEVER CANCEL** long-running builds or tests
- Set explicit timeouts: 60+ minutes for builds, 30+ minutes for tests
- **ALWAYS** validate end-to-end functionality after setup

### Manual Testing Scenarios
Currently no application exists to test. When code is added:
- **ALWAYS** test the main entry point of any application
- **ALWAYS** verify core functionality works end-to-end
- **ALWAYS** test error conditions and edge cases
- Take screenshots of UI applications when possible

## Common Tasks

### Repository Information
- **Check files**: `find . -type f | grep -v ".git" | sort`
- **Check directories**: `find . -maxdepth 2 -name ".git" -prune -o -type d -print | sort`
- **Repository size**: `du -sh .`
- **Git branch info**: `git --no-pager branch -a`

### File Operations (validated commands)
```bash
# List all files (excluding .git)
find . -name ".git" -prune -o -type f -print | sort

# Count files
find . -name ".git" -prune -o -type f -print | wc -l

# Check for specific file types
find . -name "*.js" -o -name "*.ts" -o -name "*.py" -o -name "*.json"
```

### Git Operations (all tested and timed)
```bash
# Quick status check (<0.01s)
git --no-pager status

# View recent commits (<0.01s)
git --no-pager log --oneline -5

# Show changes (<0.01s)
git --no-pager diff

# Show staged changes (<0.01s)
git --no-pager diff --cached
```

## Expected Timings
- **Git operations**: All standard git commands complete in <0.01 seconds
- **File system operations**: Directory listing and file searches complete in <0.1 seconds
- **Future builds**: When build systems are added, document actual timing with NEVER CANCEL warnings

## Important Notes
- **Repository is minimal**: Most development tools and frameworks need to be added
- **No existing CI**: GitHub Actions workflows will need to be created if automated testing is required
- **Environment is ready**: All major development tools (Node.js, Python, Git) are available
- **Always update these instructions**: When you add new capabilities, update this file with validated commands and timing information

## Getting Help
- **Repository structure**: Use `ls -la` and `find` commands documented above
- **Git status**: Use `git --no-pager status` and `git --no-pager log --oneline`
- **Available tools**: Check `which [tool]` and `[tool] --version` for any development tool
- **When in doubt**: Follow the validation requirements above before making changes