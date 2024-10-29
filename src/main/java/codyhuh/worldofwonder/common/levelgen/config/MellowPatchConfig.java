package codyhuh.worldofwonder.common.levelgen.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public record MellowPatchConfig(int tries, int xzSpread, int ySpread, Holder<PlacedFeature> feature) implements FeatureConfiguration {
    public static final Codec<MellowPatchConfig> CODEC = RecordCodecBuilder.create((p_191312_) -> {
        return p_191312_.group(ExtraCodecs.POSITIVE_INT.fieldOf("tries").orElse(128).forGetter(MellowPatchConfig::tries),
                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("xz_spread").orElse(7).forGetter(MellowPatchConfig::xzSpread),
                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("y_spread").orElse(3).forGetter(MellowPatchConfig::ySpread),
                PlacedFeature.CODEC.fieldOf("feature").forGetter(MellowPatchConfig::feature))
                .apply(p_191312_, MellowPatchConfig::new);
    });

    public MellowPatchConfig(int tries, int xzSpread, int ySpread, Holder<PlacedFeature> feature) {
        this.tries = tries;
        this.xzSpread = xzSpread;
        this.ySpread = ySpread;
        this.feature = feature;
    }

    public int tries() {
        return this.tries;
    }

    public int xzSpread() {
        return this.xzSpread;
    }

    public int ySpread() {
        return this.ySpread;
    }

    public Holder<PlacedFeature> feature() {
        return this.feature;
    }
}

