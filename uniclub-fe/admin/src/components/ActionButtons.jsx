"use client"

import { Eye, Pencil, Trash2, Layers, Loader2 } from "lucide-react"
import { useState, useEffect } from "react"

/**
 * Action button with consistent styling and states
 */
export function ActionButton({ 
  variant = "ghost", 
  size = "sm", 
  icon: Icon, 
  label, 
  onClick, 
  loading = false,
  disabled = false,
  ariaLabel,
  tooltip,
  className = "",
  shortcut = ""
}) {
  const [showTooltip, setShowTooltip] = useState(false)

  const baseStyles = "inline-flex items-center justify-center rounded-md font-medium transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-neutral-900 focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50"
  
  const sizeStyles = {
    sm: "h-8 px-2 text-xs gap-1.5",
    md: "h-9 px-3 text-sm gap-2",
  }
  
  const variantStyles = {
    ghost: "hover:bg-neutral-100 hover:text-neutral-900 active:bg-neutral-200",
    outline: "border border-neutral-200 bg-white hover:bg-neutral-50 hover:text-neutral-900 active:bg-neutral-100",
    secondary: "bg-neutral-100 text-neutral-900 hover:bg-neutral-200 active:bg-neutral-300",
    destructive: "text-red-600 hover:bg-red-50 hover:text-red-700 active:bg-red-100",
  }

  const tooltipText = shortcut ? `${tooltip} (${shortcut})` : tooltip

  return (
    <div className="relative inline-block">
      <button
        type="button"
        onClick={onClick}
        disabled={disabled || loading}
        aria-label={ariaLabel || label}
        className={`${baseStyles} ${sizeStyles[size]} ${variantStyles[variant]} ${className}`}
        onMouseEnter={() => setShowTooltip(true)}
        onMouseLeave={() => setShowTooltip(false)}
        onFocus={() => setShowTooltip(true)}
        onBlur={() => setShowTooltip(false)}
      >
        {loading ? (
          <Loader2 className="h-4 w-4 animate-spin" />
        ) : (
          <>
            {Icon && <Icon className="h-4 w-4" />}
            {label && <span className="hidden md:inline">{label}</span>}
          </>
        )}
      </button>
      
      {tooltipText && showTooltip && !loading && (
        <div className="absolute bottom-full left-1/2 -translate-x-1/2 mb-2 px-2 py-1 text-xs text-white bg-neutral-900 rounded whitespace-nowrap z-50 pointer-events-none animate-in fade-in zoom-in duration-150">
          {tooltipText}
          <div className="absolute top-full left-1/2 -translate-x-1/2 -mt-1 border-4 border-transparent border-t-neutral-900"></div>
        </div>
      )}
    </div>
  )
}

/**
 * Action buttons group for table rows
 */
export function ActionButtonGroup({ 
  onView, 
  onEdit, 
  onDelete, 
  onVariants,
  row,
  viewLabel = "Xem",
  editLabel = "Sửa", 
  deleteLabel = "Xóa",
  variantsLabel = "Biến thể",
  loading = null 
}) {
  return (
    <div className="flex items-center space-x-1" role="group" aria-label="Hành động">
      {onView && (
        <ActionButton
          variant="ghost"
          icon={Eye}
          label={viewLabel}
          onClick={() => onView(row)}
          loading={loading === 'view'}
          ariaLabel={`Xem ${row.name || row.id}`}
          tooltip="Xem chi tiết"
          shortcut="V"
        />
      )}
      
      {onEdit && (
        <ActionButton
          variant="outline"
          icon={Pencil}
          label={editLabel}
          onClick={() => onEdit(row)}
          loading={loading === 'edit'}
          ariaLabel={`Sửa ${row.name || row.id}`}
          tooltip="Chỉnh sửa"
          shortcut="E"
        />
      )}
      
      {onVariants && (
        <ActionButton
          variant="secondary"
          icon={Layers}
          label={variantsLabel}
          onClick={() => onVariants(row)}
          loading={loading === 'variants'}
          ariaLabel={`Xem biến thể của ${row.name || row.id}`}
          tooltip="Xem biến thể"
          shortcut="R"
        />
      )}
      
      {onDelete && (
        <ActionButton
          variant="destructive"
          icon={Trash2}
          label={deleteLabel}
          onClick={() => onDelete(row)}
          loading={loading === 'delete'}
          ariaLabel={`Xóa ${row.name || row.id}`}
          tooltip="Xóa"
          shortcut="Del"
        />
      )}
    </div>
  )
}

/**
 * Dropdown menu for mobile/compact views
 */
export function ActionDropdown({ 
  onView, 
  onEdit, 
  onDelete, 
  onVariants,
  row 
}) {
  const [isOpen, setIsOpen] = useState(false)

  return (
    <div className="relative md:hidden">
      <button
        onClick={() => setIsOpen(!isOpen)}
        className="p-2 hover:bg-neutral-100 rounded-md"
        aria-label="Mở menu"
      >
        <svg className="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 5v.01M12 12v.01M12 19v.01M12 6a1 1 0 110-2 1 1 0 010 2zm0 7a1 1 0 110-2 1 1 0 010 2zm0 7a1 1 0 110-2 1 1 0 010 2z" />
        </svg>
      </button>
      
      {isOpen && (
        <>
          <div 
            className="fixed inset-0 z-40" 
            onClick={() => setIsOpen(false)}
          />
          <div className="absolute right-0 mt-1 w-48 bg-white border border-neutral-200 rounded-lg shadow-lg z-50">
            {onView && (
              <button
                onClick={() => { onView(row); setIsOpen(false); }}
                className="w-full flex items-center px-4 py-2 text-sm hover:bg-neutral-50"
              >
                <Eye className="h-4 w-4 mr-2" />
                Xem
              </button>
            )}
            {onEdit && (
              <button
                onClick={() => { onEdit(row); setIsOpen(false); }}
                className="w-full flex items-center px-4 py-2 text-sm hover:bg-neutral-50"
              >
                <Pencil className="h-4 w-4 mr-2" />
                Sửa
              </button>
            )}
            {onVariants && (
              <button
                onClick={() => { onVariants(row); setIsOpen(false); }}
                className="w-full flex items-center px-4 py-2 text-sm hover:bg-neutral-50"
              >
                <Layers className="h-4 w-4 mr-2" />
                Biến thể
              </button>
            )}
            {onDelete && (
              <button
                onClick={() => { onDelete(row); setIsOpen(false); }}
                className="w-full flex items-center px-4 py-2 text-sm text-red-600 hover:bg-red-50"
              >
                <Trash2 className="h-4 w-4 mr-2" />
                Xóa
              </button>
            )}
          </div>
        </>
      )}
    </div>
  )
}