package ooga.model.sprites.animation;

import java.util.List;

public class OneShotAnimation extends SpriteAnimation {
    private final List<String> costumes;
    private final double framePeriod;
    private double animationTime;

    public OneShotAnimation(List<String> costumes, double framePeriod) {
        super(costumes.get(0));

        this.costumes = costumes;
        this.framePeriod = framePeriod;
        this.animationTime = 0;
    }

    @Override
    public void step(double dt) {
        if(!isPaused()) {
            animationTime += getRelativeSpeed() * dt;
            int costumeIndex = Math.min(costumes.size() - 1, (int)(animationTime / framePeriod));
            setCostume(costumes.get(costumeIndex));

            if(animationTime > framePeriod * costumes.size())
                notifyObservers(ao -> ao.onAnimationComplete());
        }
    }
}
