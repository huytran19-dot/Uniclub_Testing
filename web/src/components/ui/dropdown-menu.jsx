import React, { useState, useRef, useEffect } from "react"

export function DropdownMenu({ children }) {
  const [isOpen, setIsOpen] = useState(false)
  const dropdownRef = useRef(null)

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setIsOpen(false)
      }
    }

    if (isOpen) {
      document.addEventListener("mousedown", handleClickOutside)
    }

    return () => {
      document.removeEventListener("mousedown", handleClickOutside)
    }
  }, [isOpen])

  const handleClose = () => setIsOpen(false)

  return (
    <div className="relative inline-block" ref={dropdownRef}>
      {React.Children.map(children, (child) => {
        if (child.type === DropdownMenuTrigger) {
          return React.cloneElement(child, { onClick: () => setIsOpen(!isOpen) })
        }
        if (child.type === DropdownMenuContent) {
          return isOpen ? React.cloneElement(child, { onClose: handleClose }) : null
        }
        return child
      })}
    </div>
  )
}

export function DropdownMenuTrigger({ asChild, children, onClick, ...props }) {
  return React.cloneElement(children, { ...props, onClick })
}

export function DropdownMenuContent({ align = "start", className = "", children, onClose }) {
  const alignClass = align === "end" ? "right-0" : "left-0"
  
  const childrenWithClose = React.Children.map(children, (child) => {
    if (child.type === DropdownMenuItem) {
      return React.cloneElement(child, { onClose })
    }
    return child
  })
  
  return (
    <div className={`absolute ${alignClass} mt-2 min-w-40 rounded-md border border-border bg-white shadow-lg z-50 ${className}`}>
      {childrenWithClose}
    </div>
  )
}

export function DropdownMenuItem({ onClick, className = "", children, onClose }) {
  const handleClick = (e) => {
    if (onClick) onClick(e)
    if (onClose) onClose()
  }
  
  return (
    <button
      type="button"
      onClick={handleClick}
      className={`w-full text-left px-3 py-2 text-sm hover:bg-muted/40 ${className}`}
    >
      {children}
    </button>
  )
}

export function DropdownMenuSeparator() {
  return <div className="h-px bg-border my-1" />
}
