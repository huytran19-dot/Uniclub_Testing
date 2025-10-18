export function formatMoney(amount) {
  return new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
  }).format(amount)
}

export function formatDate(dateString) {
  return new Date(dateString).toLocaleDateString("vi-VN")
}

export function getStatusLabel(status) {
  const labels = {
    1: "Hoạt động",
    0: "Ẩn",
    PENDING: "Chờ xử lý",
    COMPLETED: "Hoàn thành",
    PAID: "Đã thanh toán",
    CANCELLED: "Đã hủy",
    SHIPPED: "Đã giao",
  }
  return labels[status] || status
}

export function getStatusType(status) {
  const types = {
    1: "active",
    0: "inactive",
    PENDING: "pending",
    COMPLETED: "completed",
    PAID: "paid",
    CANCELLED: "cancelled",
    SHIPPED: "shipped",
  }
  return types[status] || "inactive"
}
