public interface OMouseListener {
	default void onAnimationStart(long animationId) {}
	default void onAnimationEnd(long animationId) {}
	default void onMouseMoveAnimationStart(long animationId) {}
	default void onMouseMoveAnimationEnd(long animationId) {}
	default void onStringAnimationStart(long animationId) {}
	default void onStringAnimationEnd(long animationId) {}
}