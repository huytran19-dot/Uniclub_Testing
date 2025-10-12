"use client"

import { Label } from "@/components/ui/label"
import { Input } from "@/components/ui/input"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { cn } from "@/lib/utils"

interface FormFieldProps {
  name: string
  label: string
  type?: "text" | "email" | "tel" | "select"
  value: string
  options?: { value: string; label: string }[]
  error?: string
  required?: boolean
  onChange: (value: string) => void
  placeholder?: string
}

export function FormField({
  name,
  label,
  type = "text",
  value,
  options,
  error,
  required,
  onChange,
  placeholder,
}: FormFieldProps) {
  return (
    <div className="space-y-2">
      <Label htmlFor={name} className="text-sm font-medium text-foreground">
        {label}
        {required && <span className="text-danger ml-1">*</span>}
      </Label>

      {type === "select" && options ? (
        <Select value={value} onValueChange={onChange}>
          <SelectTrigger id={name} className={cn(error && "border-danger")}>
            <SelectValue placeholder={placeholder || `Chọn ${label.toLowerCase()}`} />
          </SelectTrigger>
          <SelectContent>
            {options.map((option) => (
              <SelectItem key={option.value} value={option.value}>
                {option.label}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
      ) : (
        <Input
          id={name}
          name={name}
          type={type}
          value={value}
          onChange={(e) => onChange(e.target.value)}
          placeholder={placeholder}
          className={cn(error && "border-danger")}
        />
      )}

      {error && <p className="text-sm text-danger">{error}</p>}
    </div>
  )
}
