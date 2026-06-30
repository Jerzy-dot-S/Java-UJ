/**
 * Pojedynczy przedziaĹ czasowy, w ktĂłrym stacja telewizyjna emituje program.
 * Slot zaczyna siÄ o godzinie atH minut atM i trwa duration minut.
 */
public record Slot( String station, String program, int atH, int atM, int duration ) {
}