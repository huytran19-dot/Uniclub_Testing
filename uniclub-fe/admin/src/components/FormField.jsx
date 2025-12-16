"use client"

export default function FormField({
  label,
  type = "text",
  value,
  onChange,
  error,
  required = false,
  placeholder,
  options,
  rows,
  disabled = false,
  hint = "",
}) {
  return (
    <div className="mb-4">
      <label className="block text-sm font-medium text-neutral-700 mb-2">
        {label}
        {required && <span className="text-red-500">*</span>}
      </label>
      {type === "select" ? (
        <select
          value={value}
          onChange={onChange}
          disabled={disabled}
          className={`w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 ${
            error ? "border-red-500" : "border-neutral-200"
          } ${disabled ? "bg-neutral-100 text-neutral-500 cursor-not-allowed" : ""}`}
        >
          <option value="">Chọn {label.toLowerCase()}</option>
          {options?.map((opt) => (
            <option key={opt.id} value={opt.id}>
              {opt.name}
            </option>
          ))}
        </select>
      ) : type === "textarea" ? (
        <textarea
          value={value}
          onChange={onChange}
          placeholder={placeholder}
          rows={rows || 4}
          disabled={disabled}
          className={`w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 ${
            error ? "border-red-500" : "border-neutral-200"
          } ${disabled ? "bg-neutral-100 text-neutral-500 cursor-not-allowed" : ""}`}
        />
      ) : type === "checkbox" ? (
        <input type="checkbox" checked={value} onChange={(e) => onChange(e.target.checked)} className="w-4 h-4" />
      ) : (
        <input
          type={type}
          value={value}
          onChange={onChange}
          placeholder={placeholder}
          disabled={disabled}
          className={`w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 ${
            error ? "border-red-500" : "border-neutral-200"
          } ${disabled ? "bg-neutral-100 text-neutral-500 cursor-not-allowed" : ""}`}
        />
      )}
      {hint && !error && <p className="text-neutral-500 text-sm mt-1 italic">{hint}</p>}
      {error && <p className="text-red-500 text-sm mt-1">{error}</p>}
    </div>
  )
}
