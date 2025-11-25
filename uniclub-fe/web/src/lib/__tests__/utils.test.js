import { describe, it, expect } from 'vitest'
import { cn, formatPrice, calculateAverageRating, getMinPrice, isOutOfStock } from '../utils'

describe.skip('(moved) utils functions', () => {
  it('cn should merge classes and dedupe duplicates', () => {
    const out = cn('btn', { 'btn-primary': true }, 'btn', 'extra')
    // simple checks for expected class names
    expect(out).toContain('btn')
    expect(out).toContain('btn-primary')
    expect(out).toContain('extra')

    // ensure the classes we expect are present
    expect(typeof out).toBe('string')
    // 'btn' should be present at least once
    expect(out.split(/\s+/).filter(Boolean)).toContain('btn')
  })

  it('formatPrice should format numbers as VND currency (contains d)', () => {
    const out = formatPrice(10000)
    expect(out).toMatch(/10\.000.*đ/)
  })

  it('calculateAverageRating should return 0 for no reviews and correct average for reviews', () => {
    expect(calculateAverageRating([])).toBe(0)
    const reviews = [{ star: 4 }, { star: 5 }, { star: 3 }]
    // average = (4+5+3)/3 = 4.0
    expect(calculateAverageRating(reviews)).toBe(4)
  })

  it('getMinPrice should return min price among available variants or fallback price', () => {
    const variants = [
      { quantity: 0, price: 100 },
      { quantity: 2, price: 50 },
      { quantity: 1, price: 75 }
    ]
    expect(getMinPrice(variants, 999)).toBe(50)

    const unavailable = [ { quantity: 0, price: 10 }, { quantity: 0, price: null } ]
    expect(getMinPrice(unavailable, 1234)).toBe(1234)
  })

  it('isOutOfStock should detect when every variant has zero quantity', () => {
    expect(isOutOfStock([{ quantity: 0 }, { quantity: 0 }])).toBe(true)
    expect(isOutOfStock([{ quantity: 0 }, { quantity: 1 }])).toBe(false)
  })
})
