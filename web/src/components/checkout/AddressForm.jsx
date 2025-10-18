import React from "react"
import { Input } from "@/components/ui/input"

export function AddressForm({ formData, onChange }) {
  const handleChange = (e) => {
    onChange({ ...formData, [e.target.name]: e.target.value })
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
          <Input name="province" value={formData.province || ""} onChange={handleChange} placeholder="TP HCM" required />
        </div>
        <div>
          <label className="block text-sm font-medium text-foreground mb-1">Quận/Huyện *</label>
          <Input name="district" value={formData.district || ""} onChange={handleChange} placeholder="Quận 1" required />
        </div>
        <div>
          <label className="block text-sm font-medium text-foreground mb-1">Phường/Xã *</label>
          <Input name="ward" value={formData.ward || ""} onChange={handleChange} placeholder="Bến Thành" required />
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
