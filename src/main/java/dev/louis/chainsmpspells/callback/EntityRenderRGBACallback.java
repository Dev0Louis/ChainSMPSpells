package dev.louis.chainsmpspells.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;

public interface EntityRenderRGBACallback {
    Event<EntityRenderRGBACallback> EVENT = EventFactory.createArrayBacked(EntityRenderRGBACallback.class, (listeners -> ((livingEntity, rgba) -> {
                for (EntityRenderRGBACallback listener : listeners) {
                    listener.interact(livingEntity, rgba);
                }
            })
    ));


    void interact(LivingEntity livingEntity, RGBA rgba);

    class RGBA {
        private float red, green, blue, alpha;
        public RGBA(float red, float green, float blue, float alpha) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = alpha;
        }
        public float getRed() {
            return red;
        }
        public float getGreen() {
            return green;
        }
        public float getBlue() {
            return blue;
        }
        public float getAlpha() {
            return alpha;
        }

        public RGBA setRed(float red) {
            this.red = red;
            return this;
        }
        public RGBA setGreen(float green) {
            this.green = green;
            return this;
        }
        public RGBA setBlue(float blue) {
            this.blue = blue;
            return this;
        }
        public RGBA setAlpha(float alpha) {
            this.alpha = alpha;
            return this;
        }

        @Override
        public String toString() {
            return "RGBA{" +
                    "red=" + red +
                    ", green=" + green +
                    ", blue=" + blue +
                    ", alpha=" + alpha +
                    '}';
        }
    }
}
