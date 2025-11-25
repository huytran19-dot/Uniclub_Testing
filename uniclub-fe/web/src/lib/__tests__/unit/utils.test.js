import { describe, it, expect } from 'vitest'
import { cn, formatPrice, calculateAverageRating, getMinPrice, isOutOfStock } from '../../utils'

describe('utils functions', () => {
  it('cn should include expected classes', () => {
    const out = cn('btn', { 'btn-primary': true }, 'btn', 'extra')
    expect(out).toContain('btn')
    expect(out).toContain('btn-primary')
    expect(out).toContain('extra')
    expect(typeof out).toBe('string')
    expect(out.split(/\s+/).filter(Boolean)).toContain('btn')
  })

  it('formatPrice should format numbers as VND currency (contains d)', () => {
    const out = formatPrice(10000)
    expect(out).toMatch(/10\.000.*đ/)
  })

  it('calculateAverageRating should return 0 for no reviews and correct average for reviews', () => {
    expect(calculateAverageRating([])).toBe(0)
    const reviews = [{ star: 4 }, { star: 5 }, { star: 3 }]
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
