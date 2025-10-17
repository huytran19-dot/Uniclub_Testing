import React, { Children, isValidElement } from "react"

function collectItems(children) {
  const items = []
  Children.forEach(children, (child) => {
    if (!isValidElement(child)) return
    if (child.type && child.type.displayName === "SelectItem") {
      items.push({ value: child.props.value, label: child.props.children })
    }
    if (child.props && child.props.children) {
      items.push(...collectItems(child.props.children))
    }
  })
  return items
}

export function Select({ value, onValueChange, children, className = "" }) {
  const items = collectItems(children)
  return (
    <select
      className={`h-9 px-3 py-2 rounded-md border border-border bg-white text-foreground text-sm ${className}`}
      value={value}
      onChange={(e) => onValueChange?.(e.target.value)}
    >
      {items.map((it) => (
        <option key={it.value} value={it.value}>
          {it.label}
        </option>
      ))}
    </select>
  )
}

export function SelectTrigger({ className = "", children, ...props }) {
  return <div className={className} {...props}>{children}</div>
}
SelectTrigger.displayName = "SelectTrigger"

export function SelectValue(props) {
  return <span {...props} />
}
SelectValue.displayName = "SelectValue"

export function SelectContent({ className = "", children, ...props }) {
  return <div className={className} {...props}>{children}</div>
}
SelectContent.displayName = "SelectContent"

export function SelectItem({ value, children, ...props }) {
  return <div data-value={value} {...props}>{children}</div>
}
SelectItem.displayName = "SelectItem"

export default Select
