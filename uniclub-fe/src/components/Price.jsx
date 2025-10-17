import React from "react"

export function Price({ value, className = "" }) {
  const formatted = new Intl.NumberFormat("vi-VN", { style: "currency", currency: "VND", maximumFractionDigits: 0 }).format(
    Number(value || 0)
  )
  return <span className={className}>{formatted}</span>
}

export default Price
