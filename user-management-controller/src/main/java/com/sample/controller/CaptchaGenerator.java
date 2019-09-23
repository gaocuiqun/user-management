package com.sample.controller;

import com.github.cage.Cage;
import com.github.cage.IGenerator;
import com.github.cage.image.Painter;
import com.github.cage.token.RandomTokenGenerator;

import java.awt.*;
import java.util.Random;

public class CaptchaGenerator extends Cage {

    /**
     * Constructor.
     *
     * @param painter
     *            to be used for painting, can be null
     * @param fonts
     *            generator used to generate fonts for texts, defaults to a
     *            random chooser from some predefined set of fonts, can be null
     * @param foregrounds
     *            generator used to generate colors for texts, defaults to a
     *            random "not-bright-so-it-is-readable-on-white" color
     *            generator, can be null
     * @param format
     *            output format, default "jpeg", can be null
     * @param compressRatio
     *            a number in [0f, 1f] interval if compression should be used
     *            with the output format. The format must support compression
     *            (like jpeg and png). If null no compression is done.
     * @param tokenGenerator
     *            a custom String token generator, can be null. If null is
     *            passed a default is created. It is not used by Cage it is only
     *            stored for convenience. Can be retrieved by
     *            {@link #getTokenGenerator()}.
     * @param rnd
     *            random generator to be used, can be null
     */
    public CaptchaGenerator(Painter painter, IGenerator<Font> fonts,
                IGenerator<Color> foregrounds, String format, Float compressRatio,
                IGenerator<String> tokenGenerator, Random rnd) {

        super(painter, fonts, foregrounds, format, compressRatio, tokenGenerator, rnd);
    }

    public static CaptchaGenerator newGenerator(int length, int delta) {
        Random rnd = new Random();
        IGenerator<String> tokenGenerator= new RandomTokenGenerator(rnd, length, delta);
        return new CaptchaGenerator(null, null, null, Cage.DEFAULT_FORMAT, Cage.DEFAULT_COMPRESS_RATIO, tokenGenerator, rnd);
    }


}
