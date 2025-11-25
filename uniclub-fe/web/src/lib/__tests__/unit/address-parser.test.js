import { describe, it, expect, vi } from 'vitest'

vi.mock('../../vietnam-provinces', async () => {
  return {
    fetchProvinces: async () => [ { code: 79, name: 'TP HCM' }, { code: 1, name: 'Ha Noi' } ],
    fetchDistricts: async (provinceCode) => {
      if (String(provinceCode) === '79') return [ { code: 776, name: 'Quan 8' }, { code: 1, name: 'District 1' } ]
      return []
    },
    fetchWards: async (districtCode) => {
      if (String(districtCode) === '776') return [ { code: 27415, name: 'Phuong 13' } ]
      return []
    }
  }
})

import { parseShippingAddress } from '../../address-parser'

describe('parseShippingAddress', () => {
  it('should return original string when shippingAddress is falsy', async () => {
    expect(await parseShippingAddress(null)).toBe(null)
    expect(await parseShippingAddress('')).toBe('')
  })

  it('should return the same string if not in old numeric-code format', async () => {
    const s = '123 Lê Lợi, Phường 1, Quận 3, TP HCM'
    const out = await parseShippingAddress(s)
    expect(out).toBe(s)
  })

  it('should translate numeric codes to names using mocked vietnam-provinces', async () => {
    const input = '22 Cao xuân dục phường 13, 27415, 776, 79'
    const out = await parseShippingAddress(input)
    expect(out).toContain('22 Cao xuân dục phường 13')
    expect(out).toContain('Phuong 13')
    expect(out).toContain('Quan 8')
    expect(out).toContain('TP HCM')
  })
})
