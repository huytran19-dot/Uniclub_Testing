const statusColors = {
  active: "bg-green-100 text-green-800",
  inactive: "bg-neutral-100 text-neutral-800",
  pending: "bg-yellow-100 text-yellow-800",
  completed: "bg-blue-100 text-blue-800",
  paid: "bg-blue-100 text-blue-800",
  cancelled: "bg-red-100 text-red-800",
  shipped: "bg-cyan-100 text-cyan-800",
}

export default function Badge({ status, label }) {
  const color = statusColors[status] || statusColors.inactive
  return <span className={`inline-block px-3 py-1 rounded-full text-sm font-medium ${color}`}>{label}</span>
}
