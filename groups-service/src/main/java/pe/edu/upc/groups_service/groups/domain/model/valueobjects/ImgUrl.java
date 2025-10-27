package pe.edu.upc.groups_service.groups.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record ImgUrl(String imgUrl) {
  public ImgUrl {
    if (imgUrl == null || imgUrl.isBlank()) {
      throw new IllegalArgumentException("Image URL cannot be null or blank");
    }
  }
}