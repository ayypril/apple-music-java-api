package pw.april.music.apple.dto;


public class EditorialNotes {
    String shortDescription;
    String standard;
    String name;
    String tagline;
    // Notes may include XML tags for formatting (<b> for bold, <i> for italic, or <br> for line break) and special characters (&amp; for &, &lt; for <, &gt; for >, &apos; for ‘, and &quot; for “).
    // https://developer.apple.com/documentation/applemusicapi/editorialnotes
}
