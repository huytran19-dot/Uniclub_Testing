// Vietnam Provinces API - Full database of provinces, districts, and wards
// API: https://provinces.open-api.vn/api/
const API_BASE = 'https://provinces.open-api.vn/api'

// Fetch all provinces
export async function fetchProvinces() {
  try {
    const response = await fetch(`${API_BASE}/p/`)
    if (!response.ok) throw new Error('Failed to fetch provinces')
    return await response.json()
  } catch (error) {
    console.error('Error fetching provinces:', error)
    return []
  }
}

// Fetch districts by province code
export async function fetchDistricts(provinceCode) {
  try {
    const response = await fetch(`${API_BASE}/p/${provinceCode}?depth=2`)
    if (!response.ok) throw new Error('Failed to fetch districts')
    const data = await response.json()
    return data.districts || []
  } catch (error) {
    console.error('Error fetching districts:', error)
    return []
  }
}

// Fetch wards by district code
export async function fetchWards(districtCode) {
  try {
    const response = await fetch(`${API_BASE}/d/${districtCode}?depth=2`)
    if (!response.ok) throw new Error('Failed to fetch wards')
    const data = await response.json()
    return data.wards || []
  } catch (error) {
    console.error('Error fetching wards:', error)
    return []
  }
}

// Fallback data in case API is unavailable
export const provinces = [
  { code: 79, name: "TP Hồ Chí Minh" },
  { code: 1, name: "Hà Nội" },
  { code: 48, name: "Đà Nẵng" },
  { code: 92, name: "Cần Thơ" },
  { code: 31, name: "Hải Phòng" },
]

// Legacy data structure (kept for backward compatibility)
export const districts = {}
export const wards = {}
