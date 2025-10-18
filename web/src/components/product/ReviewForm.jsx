import React, { useState } from "react"
import { Star, X, Upload } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { getCurrentUser } from "@/lib/auth"

export function ReviewForm({ productId, onSubmit, onCancel }) {
  const user = getCurrentUser()
  const [rating, setRating] = useState(5)
  const [hoveredRating, setHoveredRating] = useState(0)
  const [content, setContent] = useState("")
  const [images, setImages] = useState([])
  const [error, setError] = useState("")

  const handleImageUpload = (e) => {
    const files = Array.from(e.target.files)
    if (files.length + images.length > 5) {
      setError("Tối đa 5 hình ảnh")
      return
    }

    // In a real app, upload to server and get URLs
    // For now, create object URLs for preview
    const newImages = files.map((file) => URL.createObjectURL(file))
    setImages([...images, ...newImages])
    setError("")
  }

  const removeImage = (index) => {
    setImages(images.filter((_, i) => i !== index))
  }

  const handleSubmit = () => {
    if (!content.trim()) {
      setError("Vui lòng nhập nội dung đánh giá")
      return
    }

    const review = {
      id: Date.now(), // Mock ID
      id_product: productId,
      id_user: user.id,
      user_name: user.full_name || user.email,
      star: rating,
      content: content.trim(),
      images: images.join(","),
      status: 1,
      created_at: new Date().toISOString(),
    }

    onSubmit(review)
  }

  return (
    <div className="space-y-4">
      <div className="text-sm font-medium text-foreground mb-2">
        Đánh giá của bạn
      </div>

      {/* Star Rating */}
      <div className="flex items-center gap-2">
        <span className="text-sm text-muted-foreground">Chọn số sao:</span>
        <div className="flex gap-1">
          {[1, 2, 3, 4, 5].map((star) => (
            <button
              key={star}
              type="button"
              onClick={() => setRating(star)}
              onMouseEnter={() => setHoveredRating(star)}
              onMouseLeave={() => setHoveredRating(0)}
              className="transition-transform hover:scale-110"
            >
              <Star
                className={`w-8 h-8 transition-colors ${
                  star <= (hoveredRating || rating)
                    ? "fill-warning text-warning"
                    : "text-muted"
                }`}
              />
            </button>
          ))}
        </div>
        <span className="text-sm font-medium text-foreground">
          {rating === 5
            ? "Tuyệt vời"
            : rating === 4
            ? "Hài lòng"
            : rating === 3
            ? "Bình thường"
            : rating === 2
            ? "Không hài lòng"
            : "Rất tệ"}
        </span>
      </div>

      {/* Content */}
      <div>
        <textarea
          value={content}
          onChange={(e) => {
            setContent(e.target.value)
            setError("")
          }}
          placeholder="Chia sẻ trải nghiệm của bạn về sản phẩm này..."
          className="w-full min-h-[120px] px-4 py-3 border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary resize-none"
          maxLength={255}
        />
        <div className="text-xs text-muted-foreground text-right mt-1">
          {content.length}/255
        </div>
      </div>

      {/* Image Upload */}
      <div>
        <label className="text-sm text-muted-foreground mb-2 block">
          Thêm hình ảnh (tối đa 5)
        </label>
        <div className="flex flex-wrap gap-2">
          {images.map((img, index) => (
            <div key={index} className="relative w-20 h-20 rounded-lg overflow-hidden border border-border group">
              <img src={img} alt={`Upload ${index + 1}`} className="w-full h-full object-cover" />
              <button
                type="button"
                onClick={() => removeImage(index)}
                className="absolute top-1 right-1 w-6 h-6 bg-black/60 rounded-full flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity"
              >
                <X className="w-4 h-4 text-white" />
              </button>
            </div>
          ))}
          {images.length < 5 && (
            <label className="w-20 h-20 border-2 border-dashed border-border rounded-lg flex flex-col items-center justify-center cursor-pointer hover:border-primary hover:bg-primary/5 transition-colors">
              <Upload className="w-5 h-5 text-muted-foreground mb-1" />
              <span className="text-xs text-muted-foreground">Tải lên</span>
              <input
                type="file"
                accept="image/*"
                multiple
                onChange={handleImageUpload}
                className="hidden"
              />
            </label>
          )}
        </div>
      </div>

      {/* Error */}
      {error && (
        <div className="text-sm text-destructive">{error}</div>
      )}

      {/* Actions */}
      <div className="flex gap-3 pt-2">
        <Button variant="outline" onClick={onCancel} className="flex-1">
          Hủy
        </Button>
        <Button onClick={handleSubmit} className="flex-1">
          Gửi đánh giá
        </Button>
      </div>
    </div>
  )
}
