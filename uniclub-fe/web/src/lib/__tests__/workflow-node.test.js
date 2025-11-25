import { describe, it, expect } from 'vitest'
import fs from 'fs'
import path from 'path'
import yaml from 'js-yaml'

describe.skip('(moved) CI workflow: node.js.yml (unit)', () => {
  const nodeWorkflowPath = path.resolve(process.cwd(), '../../.github/workflows/node.js.yml')
  it('should parse and have expected top-level fields', () => {
    const content = fs.readFileSync(nodeWorkflowPath, 'utf8')
    const obj = yaml.load(content)

    expect(obj).toBeTruthy()
    expect(obj.name).toBe('Node.js CI')

    // triggers
    expect(obj.on).toBeTruthy()
    expect(obj.on.push).toBeTruthy()
    expect(obj.on.push.branches).toContain('main')
    expect(obj.on.pull_request).toBeTruthy()
    expect(obj.on.pull_request.branches).toContain('main')

    // job config
    expect(obj.jobs).toBeTruthy()
    expect(obj.jobs.build).toBeTruthy()
    expect(obj.jobs.build['runs-on']).toBe('ubuntu-latest')

    // matrix node versions
    const matrix = obj.jobs.build.strategy.matrix
    expect(matrix).toBeTruthy()
    expect(matrix['node-version']).toEqual(expect.arrayContaining(['18.x', '20.x', '22.x']))
  })

  it('should contain key steps including checkout, setup-node and install/test commands', () => {
    const content = fs.readFileSync(nodeWorkflowPath, 'utf8')
    const obj = yaml.load(content)
    const steps = obj.jobs.build.steps

    // check for checkout step
    expect(steps.some(s => s && s.uses && String(s.uses).startsWith('actions/checkout'))).toBe(true)

    // check for setup-node usage
    expect(steps.some(s => s && s.uses && String(s.uses).startsWith('actions/setup-node'))).toBe(true)

    // check for npm ci, build and test runs
    const runCommands = steps.filter(s => s && s.run).map(s => s.run)
    expect(runCommands).toContain('npm ci')
    expect(runCommands).toContain('npm run build --if-present')
    expect(runCommands).toContain('npm test')
  })
})
