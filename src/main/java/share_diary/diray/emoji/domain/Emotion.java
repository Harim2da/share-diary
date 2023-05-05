package share_diary.diray.emoji.domain;

public enum Emotion {

    SMILE("웃음"),
    SAD("슬픔"),
    ANGRY("화남"),
    HAPPY("행복"),
    END("끝");

    private final String name;

    Emotion(String name) {
        this.name = name;
    }
}
