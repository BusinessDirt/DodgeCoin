package businessdirt.dodgecoin.core.util;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.core.Config;
import businessdirt.dodgecoin.core.config.data.Property;
import com.badlogic.gdx.audio.Music;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class MusicPlayer {

    // all musics that are being played by the player
    private static final List<Music> musics = new ArrayList<>();

    // the field of the volume in the config file
    // needs to be a field in case the volume is changed
    private static Field volume;

    public MusicPlayer(String path) {
        // init the volume
        if (volume == null) MusicPlayer.initVolume();

        // configure the music (set volume, set loop, play it, add to list)
        Music music = DodgeCoin.assets.getMusic(path);
        music.setLooping(true);
        music.play();
        musics.add(music);
    }

    private static void initVolume() {
        // get all the declared fields in the Config class
        // only the fields that have the Property annotation will be in the list
        List<Field> filteredFields = Arrays.stream(Config.class.getDeclaredFields()).filter(field -> field.isAnnotationPresent(Property.class)).collect(Collectors.toList());

        // find the musicVolume field
        filteredFields.forEach(field -> {
            field.setAccessible(true);
            if (field.getName().equals("musicVolume")) volume = field;
        });

        // every millisecond it will execute the timer task that updates the volume
        new Timer("", false).scheduleAtFixedRate(new UpdateVolumeTimerTask(volume), 0L, 1L);
    }

    private static class UpdateVolumeTimerTask extends TimerTask {

        // volume field
        private final Field volume;

        public UpdateVolumeTimerTask(Field volume) {
            this.volume = volume;
        }

        @Override
        public void run() {
            try {
                // sets the volume of all music files to the fields value
                int vol = (Integer) volume.get(volume);
                MusicPlayer.musics.forEach(music -> music.setVolume(vol / 100f));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
