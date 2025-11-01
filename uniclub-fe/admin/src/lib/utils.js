export function formatMoney(amount) {
  return new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
  }).format(amount)
}

export function formatDate(dateString) {
  return new Date(dateString).toLocaleDateString("vi-VN")
}

export function formatDateTime(dateString) {
  if (!dateString) return "";
  // toLocaleString sẽ lấy cả ngày và giờ
  return new Date(dateString).toLocaleString("vi-VN");
}

export function getStatusLabel(status) {
  const labels = {
    1: "Hoạt động",
    0: "Ẩn",
    PENDING: "Chờ xử lý",
    PAID: "Đã thanh toán",
    CONFIRMED: "Đã xác nhận",
    SHIPPING: "Đang giao",
    SHIPPED: "Đang giao",
    DELIVERED: "Đã giao",
    CANCELLED: "Đã hủy",
  }
  return labels[status] || status
}

export function getStatusType(status) {
  const types = {
    1: "active",
    0: "inactive",
    PENDING: "pending",
    CONFIRMED: "confirmed",
    SHIPPING: "shipping",
    DELIVERED: "delivered",
    CANCELLED: "cancelled",
  }
  return types[status] || "inactive"
}
