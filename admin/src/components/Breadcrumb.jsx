import { ChevronRight } from "lucide-react"
import { Link } from "react-router-dom"

export default function Breadcrumb({ items }) {
  return (
    <div className="flex items-center gap-2 text-sm text-neutral-600 mb-6">
      {items.map((item, idx) => (
        <div key={idx} className="flex items-center gap-2">
          {item.path ? (
            <Link to={item.path} className="text-blue-600 hover:underline">
              {item.label}
            </Link>
          ) : (
            <span>{item.label}</span>
          )}
          {idx < items.length - 1 && <ChevronRight size={16} />}
        </div>
      ))}
    </div>
  )
}
