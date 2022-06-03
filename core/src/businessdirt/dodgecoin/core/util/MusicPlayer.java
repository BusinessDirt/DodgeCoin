package businessdirt.dodgecoin.core.util;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.core.Config;
import businessdirt.dodgecoin.core.config.data.Property;
import com.badlogic.gdx.audio.Music;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class MusicPlayer {

    private static final List<Music> musics = new ArrayList<>();
    private static Field volume;

    public MusicPlayer(String path) {
        if (volume == null) MusicPlayer.initVolume();
        Music music = DodgeCoin.assets.getMusic(path);
        music.setLooping(true);
        music.play();
        musics.add(music);
    }

    private static void initVolume() {
        List<Field> filteredFields = Arrays.stream(Config.class.getDeclaredFields()).filter(field -> field.isAnnotationPresent(Property.class)).collect(Collectors.toList());
        filteredFields.forEach(field -> {
            field.setAccessible(true);
            if (field.getName().equals("musicVolume")) volume = field;
        });
        new Timer("", false).scheduleAtFixedRate(new UpdateVolumeTimerTask(volume), 0L, 1L);
    }

    private static class UpdateVolumeTimerTask extends TimerTask {

        private final Field volume;

        public UpdateVolumeTimerTask(Field volume) {
            this.volume = volume;
        }

        @Override
        public void run() {
            try {
                int vol = (Integer) volume.get(volume);
                MusicPlayer.musics.forEach(music -> music.setVolume(vol / 100f));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
