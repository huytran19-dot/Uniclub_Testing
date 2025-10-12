"use client"

import { FormField } from "./form-field"

interface VNAddressPickerProps {
  province: string
  district: string
  ward: string
  onChange: (address: { province: string; district: string; ward: string }) => void
  errors?: {
    province?: string
    district?: string
    ward?: string
  }
}

// Mock Vietnamese provinces, districts, wards
const provinces = [
  { value: "TP HCM", label: "TP Hồ Chí Minh" },
  { value: "Hà Nội", label: "Hà Nội" },
  { value: "Đà Nẵng", label: "Đà Nẵng" },
  { value: "Cần Thơ", label: "Cần Thơ" },
]

const districts: Record<string, { value: string; label: string }[]> = {
  "TP HCM": [
    { value: "Quận 1", label: "Quận 1" },
    { value: "Quận 2", label: "Quận 2" },
    { value: "Quận 3", label: "Quận 3" },
  ],
  "Hà Nội": [
    { value: "Ba Đình", label: "Ba Đình" },
    { value: "Hoàn Kiếm", label: "Hoàn Kiếm" },
    { value: "Đống Đa", label: "Đống Đa" },
  ],
  "Đà Nẵng": [
    { value: "Hải Châu", label: "Hải Châu" },
    { value: "Thanh Khê", label: "Thanh Khê" },
  ],
  "Cần Thơ": [
    { value: "Ninh Kiều", label: "Ninh Kiều" },
    { value: "Cái Răng", label: "Cái Răng" },
  ],
}

const wards: Record<string, { value: string; label: string }[]> = {
  "Quận 1": [
    { value: "Bến Nghé", label: "Bến Nghé" },
    { value: "Bến Thành", label: "Bến Thành" },
  ],
  "Quận 2": [
    { value: "Thảo Điền", label: "Thảo Điền" },
    { value: "An Phú", label: "An Phú" },
  ],
  "Ba Đình": [
    { value: "Phúc Xá", label: "Phúc Xá" },
    { value: "Trúc Bạch", label: "Trúc Bạch" },
  ],
  "Hải Châu": [
    { value: "Thạch Thang", label: "Thạch Thang" },
    { value: "Hải Châu 1", label: "Hải Châu 1" },
  ],
  "Ninh Kiều": [
    { value: "Tân An", label: "Tân An" },
    { value: "An Hòa", label: "An Hòa" },
  ],
}

export function VNAddressPicker({ province, district, ward, onChange, errors }: VNAddressPickerProps) {
  const availableDistricts = province ? districts[province] || [] : []
  // const availableWards = district ? wards[district] || [] : []

  const handleProvinceChange = (newProvince: string) => {
    onChange({ province: newProvince, district: "", ward: "" })
  }

  const handleDistrictChange = (newDistrict: string) => {
    onChange({ province, district: newDistrict, ward: "" })
  }

  const handleWardChange = (newWard: string) => {
    onChange({ province, district, ward: newWard })
  }

  return (
    <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
      <FormField
        name="province"
        label="Tỉnh/Thành phố"
        type="select"
        value={province}
        options={provinces}
        onChange={handleProvinceChange}
        error={errors?.province}
        required
      />

      <FormField
        name="district"
        label="Quận/Huyện"
        type="select"
        value={district}
        options={availableDistricts}
        onChange={handleDistrictChange}
        error={errors?.district}
        required
        placeholder={province ? "Chọn quận/huyện" : "Chọn tỉnh/thành trước"}
      />

      <FormField
        name="ward"
        label="Phường/Xã"
        type="text"
        value={ward}
        onChange={handleWardChange}
        error={errors?.ward}
        required
        placeholder="Nhập phường/xã"
      />
    </div>
  )
}
