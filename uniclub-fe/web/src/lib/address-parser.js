// Helper to check if a string looks like a code (all digits)
const isCode = (str) => /^\d+$/.test(str?.trim())

// Parse shipping address and convert codes to names if needed
export async function parseShippingAddress(shippingAddress) {
  if (!shippingAddress) return shippingAddress

  const parts = shippingAddress.split(',').map(s => s.trim())

  // If less than 4 parts or last 3 parts don't look like codes, return as is
  if (parts.length < 4 || !parts.slice(-3).every(isCode)) {
    console.log('Not old format, returning as is')
    return shippingAddress
  }

  // Extract: parts should be [street, ward, district, province] but all as codes in last 3
  // Example: "22 Cao xuân dục phường 13, quận 8, 27415, 776, 79"
  // So we need to find where codes start

  let wardCode, districtCode, provinceCode
  let addressParts = []

  // Last 3 should be codes
  provinceCode = parts[parts.length - 1]
  districtCode = parts[parts.length - 2]
  wardCode = parts[parts.length - 3]

  // Everything before that is address
  addressParts = parts.slice(0, -3)


  try {
    // Dynamically import to avoid bundling issues
    const { fetchProvinces, fetchDistricts, fetchWards } = await import('./vietnam-provinces')

    // Fetch province name
    const provinces = await fetchProvinces()
    // Convert code to number for comparison
    const province = provinces.find(p => String(p.code) === provinceCode)
    const provinceName = province?.name || provinceCode

    // Fetch district name
    const districts = await fetchDistricts(provinceCode)
    const district = districts.find(d => String(d.code) === districtCode)
    const districtName = district?.name || districtCode

    // Fetch ward name
    const wards = await fetchWards(districtCode)
    const ward = wards.find(w => String(w.code) === wardCode)
    const wardName = ward?.name || wardCode


    // Reconstruct address with names
    const result = [...addressParts, wardName, districtName, provinceName].join(', ')
    return result
  } catch (error) {
    console.error('Error parsing address:', error)
    return shippingAddress
  }
}
