"use client"

export default function Confirm({ isOpen, title, message, onConfirm, onCancel }) {
  if (!isOpen) return null

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-lg max-w-sm w-full mx-4">
        <div className="p-6">
          <h2 className="text-lg font-semibold mb-2">{title}</h2>
          <p className="text-neutral-600 mb-6">{message}</p>
          <div className="flex gap-3">
            <button
              onClick={onCancel}
              className="flex-1 px-4 py-2 border border-neutral-200 rounded-lg hover:bg-neutral-50"
            >
              Hủy
            </button>
            <button onClick={onConfirm} className="flex-1 px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700">
              Xác nhận
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}
