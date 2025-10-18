"use client"

import { X } from "lucide-react"
import { useEffect } from "react"

export default function Toast({ message, type = "success", onClose }) {
  useEffect(() => {
    const timer = setTimeout(onClose, 3000)
    return () => clearTimeout(timer)
  }, [onClose])

  const bgColor = {
    success: "bg-green-100 text-green-800",
    error: "bg-red-100 text-red-800",
    info: "bg-blue-100 text-blue-800",
  }[type]

  return (
    <div className={`fixed bottom-4 right-4 p-4 rounded-lg shadow-lg flex items-center gap-3 ${bgColor} z-50`}>
      <span>{message}</span>
      <button onClick={onClose} className="p-1 hover:bg-black/10 rounded">
        <X size={16} />
      </button>
    </div>
  )
}
