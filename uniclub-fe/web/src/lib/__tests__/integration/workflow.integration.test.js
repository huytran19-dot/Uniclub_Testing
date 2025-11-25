import { describe, it, expect } from 'vitest'
import fs from 'fs'
import path from 'path'
import yaml from 'js-yaml'

describe('CI workflow integration checks', () => {
  it('web-tests.yml should reference the web package working-directory correctly', () => {
    const wfPath = path.resolve(process.cwd(), '../../.github/workflows/web-tests.yml')
    const content = fs.readFileSync(wfPath, 'utf8')
    const obj = yaml.load(content)

    expect(obj).toBeTruthy()
    // workflow was reorganized into two jobs: unit-tests and integration-tests
    expect(obj.jobs).toBeTruthy()
    const unitJob = obj.jobs['unit-tests'] || obj.jobs['web-tests']
    const integrationJob = obj.jobs['integration-tests'] || null
    // prefer the integration job for this assertion (if present), otherwise check web-tests job
    const steps = (integrationJob && integrationJob.steps) || (unitJob && unitJob.steps)
    expect(Array.isArray(steps)).toBe(true)

    const workingDirectories = steps.map(s => s['working-directory']).filter(Boolean)
    expect(workingDirectories).toContain('uniclub-fe/web')

    const runSteps = steps.filter(s => s.run).map(s => s.run)
    expect(runSteps.some(r => r.includes('npm') && r.includes('test'))).toBe(true)
  })

  it('web package must have a test script and vitest devDependency', () => {
    const p = fs.readFileSync(path.resolve(process.cwd(), './package.json'), 'utf8')
    const pkg = JSON.parse(p)
    expect(pkg.scripts).toBeTruthy()
    expect(pkg.scripts.test).toBeTruthy()
    expect(pkg.devDependencies).toBeTruthy()
    expect(pkg.devDependencies.vitest || pkg.devDependencies['vitest']).toBeTruthy()
  })

  it('should require a package-lock.json for reproducible CI', () => {
    const lockPath = path.resolve(process.cwd(), './package-lock.json')
    const exists = fs.existsSync(lockPath)
    expect(exists).toBe(true)
  })
})
