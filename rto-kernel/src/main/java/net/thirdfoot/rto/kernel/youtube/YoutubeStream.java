package net.thirdfoot.rto.kernel.youtube;

/**
 * @author lcsontos
 */
public interface YoutubeStream {

    public String getExtension();

    public String getMediaType();

    public String getResolution();

    public String getQuality();

    public int getSize();

    public String getUrl();

}