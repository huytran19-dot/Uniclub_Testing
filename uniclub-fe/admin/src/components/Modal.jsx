"use client"

import { X } from "lucide-react"

export default function Modal({ isOpen, title, children, onClose, onSubmit, submitLabel = "Lưu" }) {
  if (!isOpen) return null

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-lg max-w-md w-full mx-4">
        <div className="flex items-center justify-between p-6 border-b border-neutral-200">
          <h2 className="text-lg font-semibold">{title}</h2>
          <button onClick={onClose} className="p-1 hover:bg-neutral-100 rounded-lg">
            <X size={20} />
          </button>
        </div>
        <div className="p-6">{children}</div>
        <div className="flex gap-3 p-6 border-t border-neutral-200">
          <button
            onClick={onClose}
            className="flex-1 px-4 py-2 border border-neutral-200 rounded-lg hover:bg-neutral-50"
          >
            Hủy
          </button>
          <button onClick={onSubmit} className="flex-1 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
            {submitLabel}
          </button>
        </div>
      </div>
    </div>
  )
}
