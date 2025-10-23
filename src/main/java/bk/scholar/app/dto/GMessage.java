package bk.scholar.app.dto;

public record GMessage(String id, String info, String comment) {
  public GMessage withComment(String newComment) {
    return new GMessage(this.id, this.info, newComment);
  }
}
