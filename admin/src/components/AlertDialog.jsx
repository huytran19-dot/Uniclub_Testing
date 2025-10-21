"use client"

import { AlertTriangle } from "lucide-react"
import { useEffect, useRef } from "react"

export default function AlertDialog({ 
  isOpen, 
  onClose, 
  onConfirm, 
  title, 
  description,
  itemName,
  itemSku,
  confirmText = "Xóa",
  cancelText = "Hủy",
  variant = "destructive" 
}) {
  const cancelRef = useRef(null)
  const confirmRef = useRef(null)

  useEffect(() => {
    if (isOpen) {
      // Focus cancel button by default for safety
      cancelRef.current?.focus()
      
      // Handle keyboard shortcuts
      const handleKeyDown = (e) => {
        if (e.key === 'Escape') {
          onClose()
        } else if (e.key === 'Enter' && e.metaKey) {
          // Cmd/Ctrl + Enter to confirm
          onConfirm()
        }
      }
      
      document.addEventListener('keydown', handleKeyDown)
      return () => document.removeEventListener('keydown', handleKeyDown)
    }
  }, [isOpen, onClose, onConfirm])

  if (!isOpen) return null

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center">
      {/* Backdrop */}
      <div 
        className="absolute inset-0 bg-black/50 backdrop-blur-sm animate-in fade-in duration-200"
        onClick={onClose}
        aria-hidden="true"
      />
      
      {/* Dialog */}
      <div 
        className="relative bg-white rounded-lg shadow-xl max-w-md w-full mx-4 animate-in fade-in zoom-in duration-200"
        role="alertdialog"
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
      >
        <div className="p-6">
          {/* Icon */}
          <div className="flex items-center justify-center w-12 h-12 mx-auto mb-4 rounded-full bg-red-50">
            <AlertTriangle className="h-6 w-6 text-red-600" />
          </div>
          
          {/* Title */}
          <h2 
            id="alert-dialog-title"
            className="text-lg font-semibold text-center text-neutral-900 mb-2"
          >
            {title}
          </h2>
          
          {/* Description */}
          <div 
            id="alert-dialog-description"
            className="text-sm text-neutral-600 text-center mb-6 space-y-2"
          >
            <p>{description}</p>
            {itemName && (
              <p className="font-medium text-neutral-900">
                {itemName}
              </p>
            )}
            {itemSku && (
              <p className="text-xs font-mono text-neutral-500">
                SKU: {itemSku}
              </p>
            )}
          </div>
          
          {/* Actions */}
          <div className="flex gap-3">
            <button
              ref={cancelRef}
              onClick={onClose}
              className="flex-1 px-4 py-2.5 text-sm font-medium border border-neutral-200 rounded-lg hover:bg-neutral-50 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-offset-2 focus-visible:ring-neutral-900 transition-colors"
              aria-label={`${cancelText} (Esc)`}
            >
              {cancelText}
            </button>
            <button
              ref={confirmRef}
              onClick={onConfirm}
              className="flex-1 px-4 py-2.5 text-sm font-medium bg-red-600 text-white rounded-lg hover:bg-red-700 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-offset-2 focus-visible:ring-red-600 transition-colors active:bg-red-800"
              aria-label={`${confirmText} (Cmd+Enter)`}
            >
              {confirmText}
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}
