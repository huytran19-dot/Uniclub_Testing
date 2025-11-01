import React, { useState, useEffect } from "react"
import { Input } from "@/components/ui/input"
import { fetchProvinces, fetchDistricts, fetchWards } from "@/lib/vietnam-provinces"

export function AddressForm({ formData, onChange }) {
  const [provinces, setProvinces] = useState([])
  const [availableDistricts, setAvailableDistricts] = useState([])
  const [availableWards, setAvailableWards] = useState([])
  const [loading, setLoading] = useState({
    provinces: false,
    districts: false,
    wards: false
  })

  // Fetch provinces on mount
  useEffect(() => {
    const loadProvinces = async () => {
      setLoading(prev => ({ ...prev, provinces: true }))
      const data = await fetchProvinces()
      setProvinces(data)
      setLoading(prev => ({ ...prev, provinces: false }))
    }
    loadProvinces()
  }, [])

  // Load districts when province is selected or on init if formData has province
  useEffect(() => {
    const loadDistricts = async () => {
      if (formData.province) {
        setLoading(prev => ({ ...prev, districts: true }))
        const data = await fetchDistricts(formData.province)
        setAvailableDistricts(data)
        setLoading(prev => ({ ...prev, districts: false }))
      } else {
        setAvailableDistricts([])
      }
    }
    loadDistricts()
  }, [formData.province])

  // Load wards when district is selected or on init if formData has district
  useEffect(() => {
    const loadWards = async () => {
      if (formData.district) {
        setLoading(prev => ({ ...prev, wards: true }))
        const data = await fetchWards(formData.district)
        setAvailableWards(data)
        setLoading(prev => ({ ...prev, wards: false }))
      } else {
        setAvailableWards([])
      }
    }
    loadWards()
  }, [formData.district])

  const handleChange = (e) => {
    const { name, value } = e.target
    
    // When province changes, reset district and ward
    if (name === "province") {
      onChange({ ...formData, province: value, district: "", ward: "" })
    }
    // When district changes, reset ward
    else if (name === "district") {
      onChange({ ...formData, district: value, ward: "" })
    }
    else {
      onChange({ ...formData, [name]: value })
    }
  }

  return (
    <div className="space-y-4">
      <div>
        <label className="block text-sm font-medium text-foreground mb-1">Họ và tên *</label>
        <Input name="full_name" value={formData.full_name || ""} onChange={handleChange} placeholder="Nguyễn Văn A" required />
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label className="block text-sm font-medium text-foreground mb-1">Số điện thoại *</label>
          <Input name="phone" value={formData.phone || ""} onChange={handleChange} placeholder="0901234567" required />
        </div>
        <div>
          <label className="block text-sm font-medium text-foreground mb-1">Email</label>
          <Input name="email" type="email" value={formData.email || ""} onChange={handleChange} placeholder="email@example.com" />
        </div>
      </div>
      <div>
        <label className="block text-sm font-medium text-foreground mb-1">Địa chỉ *</label>
        <Input name="address" value={formData.address || ""} onChange={handleChange} placeholder="123 Lê Lợi" required />
      </div>
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div>
          <label className="block text-sm font-medium text-foreground mb-1">Tỉnh/Thành phố *</label>
          <select
            name="province"
            value={formData.province || ""}
            onChange={handleChange}
            required
            disabled={loading.provinces}
            className="w-full px-3 py-2 rounded-md border border-border bg-white text-foreground text-sm outline-none focus:ring-2 focus:ring-ring disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <option value="">{loading.provinces ? "Đang tải..." : "Chọn Tỉnh/Thành phố"}</option>
            {provinces.map((province) => (
              <option key={province.code} value={province.code}>
                {province.name}
              </option>
            ))}
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium text-foreground mb-1">Quận/Huyện *</label>
          <select
            name="district"
            value={formData.district || ""}
            onChange={handleChange}
            required
            disabled={!formData.province || loading.districts}
            className="w-full px-3 py-2 rounded-md border border-border bg-white text-foreground text-sm outline-none focus:ring-2 focus:ring-ring disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <option value="">{loading.districts ? "Đang tải..." : "Chọn Quận/Huyện"}</option>
            {availableDistricts.map((district) => (
              <option key={district.code} value={district.code}>
                {district.name}
              </option>
            ))}
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium text-foreground mb-1">Phường/Xã *</label>
          <select
            name="ward"
            value={formData.ward || ""}
            onChange={handleChange}
            required
            disabled={!formData.district || loading.wards}
            className="w-full px-3 py-2 rounded-md border border-border bg-white text-foreground text-sm outline-none focus:ring-2 focus:ring-ring disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <option value="">{loading.wards ? "Đang tải..." : "Chọn Phường/Xã"}</option>
            {availableWards.map((ward) => (
              <option key={ward.code} value={ward.code}>
                {ward.name}
              </option>
            ))}
          </select>
        </div>
      </div>
      <div>
        <label className="block text-sm font-medium text-foreground mb-1">Ghi chú</label>
        <textarea
          name="note"
          value={formData.note || ""}
          onChange={handleChange}
          placeholder="Ghi chú cho đơn hàng (tùy chọn)"
          rows={3}
          className="w-full px-3 py-2 rounded-md border border-border bg-white text-foreground text-sm outline-none focus:ring-2 focus:ring-ring"
        />
      </div>
    </div>
  )
}
