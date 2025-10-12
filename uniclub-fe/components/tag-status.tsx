import { cn } from "@/lib/utils"

interface TagStatusProps {
  value: string
  className?: string
}

export function TagStatus({ value, className }: TagStatusProps) {
  const statusConfig: Record<string, { label: string; color: string }> = {
    PENDING: { label: "Chờ xử lý", color: "bg-yellow-100 text-yellow-800" },
    PAID: { label: "Đã thanh toán", color: "bg-green-100 text-green-800" },
    CANCELLED: { label: "Đã hủy", color: "bg-red-100 text-red-800" },
    SHIPPED: { label: "Đang giao", color: "bg-blue-100 text-blue-800" },
  }

  const config = statusConfig[value] || { label: value, color: "bg-gray-100 text-gray-800" }

  return (
    <span
      className={cn("inline-flex items-center px-3 py-1 rounded-full text-sm font-medium", config.color, className)}
    >
      {config.label}
    </span>
  )
}
