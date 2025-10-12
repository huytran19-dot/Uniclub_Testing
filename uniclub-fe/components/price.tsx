interface PriceProps {
  value: number
  className?: string
}

export function Price({ value, className = "" }: PriceProps) {
  const formatted = new Intl.NumberFormat("vi-VN").format(value)

  return <span className={className}>{formatted}đ</span>
}
