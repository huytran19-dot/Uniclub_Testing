import React, { useState, useEffect } from "react"
import { Input } from "@/components/ui/input"
import { fetchProvinces, fetchDistricts, fetchWards } from "@/lib/vietnam-provinces"

export function AddressForm({ formData, onChange, hidePersonalInfo = false }) {
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

  // Populate province name when province code exists but name is empty
  useEffect(() => {
    if (formData.province && provinces.length > 0 && !formData.provinceName) {
      const selectedProvince = provinces.find(p => String(p.code) === String(formData.province))
      if (selectedProvince) {
        console.log("Auto-populating provinceName:", selectedProvince.name)
        onChange({ ...formData, provinceName: selectedProvince.name })
      }
    }
  }, [provinces.length, formData.province, formData.provinceName])

  // Populate district name when district code exists but name is empty
  useEffect(() => {
    if (formData.district && availableDistricts.length > 0 && !formData.districtName) {
      const selectedDistrict = availableDistricts.find(d => String(d.code) === String(formData.district))
      if (selectedDistrict) {
        console.log("Auto-populating districtName:", selectedDistrict.name)
        onChange({ ...formData, districtName: selectedDistrict.name })
      }
    }
  }, [availableDistricts.length, formData.district, formData.districtName])

  // Populate ward name when ward code exists but name is empty
  useEffect(() => {
    if (formData.ward && availableWards.length > 0 && !formData.wardName) {
      const selectedWard = availableWards.find(w => String(w.code) === String(formData.ward))
      if (selectedWard) {
        console.log("Auto-populating wardName:", selectedWard.name)
        onChange({ ...formData, wardName: selectedWard.name })
      }
    }
  }, [availableWards.length, formData.ward, formData.wardName])

  const handleChange = (e) => {
    const { name, value } = e.target
    
    // When province changes, reset district and ward
    if (name === "province") {
      const selectedProvince = provinces.find(p => String(p.code) === String(value))
      console.log("Selected province:", selectedProvince, "from value:", value)
      onChange({ 
        ...formData, 
        province: value, 
        provinceName: selectedProvince?.name || "",
        district: "", 
        districtName: "",
        ward: "",
        wardName: ""
      })
    }
    // When district changes, reset ward
    else if (name === "district") {
      const selectedDistrict = availableDistricts.find(d => String(d.code) === String(value))
      console.log("Selected district:", selectedDistrict, "from value:", value)
      onChange({ 
        ...formData, 
        district: value,
        districtName: selectedDistrict?.name || "",
        ward: "",
        wardName: ""
      })
    }
    else if (name === "ward") {
      const selectedWard = availableWards.find(w => String(w.code) === String(value))
      console.log("Selected ward:", selectedWard, "from value:", value)
      onChange({ 
        ...formData, 
        ward: value,
        wardName: selectedWard?.name || ""
      })
    }
    else {
      onChange({ ...formData, [name]: value })
    }
  }

  return (
    <div className="space-y-4">
      {!hidePersonalInfo && (
        <>
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
        </>
      )}
      {hidePersonalInfo && (
        <div>
          <label className="block text-sm font-medium text-foreground mb-1">Số điện thoại *</label>
          <Input name="phone" value={formData.phone || ""} onChange={handleChange} placeholder="0901234567" required />
        </div>
      )}
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
      {!hidePersonalInfo && (
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
      )}
    </div>
  )
}
