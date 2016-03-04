package common;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.Serializable;

public class ID3Tag implements Serializable {
	
	/**
     * 
     */
    private static final long serialVersionUID = -5233207519609692173L;
	private String title;
	private String artist;
	private String album;
	private int year;
	private String comment;
	private int genre;
	
	private ID3Tag()
	{
	}
	
	private static byte[] readXBytes(byte[] byteArray, int fromPos, int toPos)
	{
		byte[] result = new byte[toPos - fromPos];
		for(int i = fromPos; i < toPos; i++ )
		{
			result[i - fromPos] = byteArray[i];
		}
		return result;
	}
	
	public static ID3Tag parse(File file)
	{
		byte[] bytes = tail(file);
		String title = new String(readXBytes(bytes, 3, 33)).trim();
		String artist = new String(readXBytes(bytes, 33, 63)).trim();
		String album = new String(readXBytes(bytes, 63, 93)).trim();
		String strYear = new String(readXBytes(bytes, 93, 97)).trim();
		int year = strYear.length() != 0 ? Integer.parseInt(strYear) : 0;
		String comment = new String(readXBytes(bytes, 97, 127)).trim();
		String genreTry = new String((readXBytes(bytes, 127, 128)));
		Integer genre;
		if (genreTry.length() > 0) {
			byte b = readXBytes(bytes, 127, 128)[0];
			genre = ((int) b > 0) ? (int) b : (int) b + 256 ;
		}
		else {
			genre = 12;
		}
		if (genre > 191) {genre = 12;}
		ID3Tag tag = new ID3Tag();
		tag.setTitle(title);
		tag.setArtist(artist);
		tag.setAlbum(album);
		tag.setYear(year);
		tag.setComment(comment);
		tag.setGenre(genre);
		return tag;
	}
	
	public static byte[] tail(File file)
    {
        try
        {
            RandomAccessFile fileHandler = new RandomAccessFile(file, "r");
            long fileLength = fileHandler.length() - 1;
            byte[] buffer = new byte[128];

            for (int i = 0; i < buffer.length; i++)
            {
                fileHandler.seek(fileLength - 127 + i);
                buffer[i] = fileHandler.readByte();
            }
            fileHandler.close();
            return buffer;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getGenre() {
		return genre;
	}

	public void setGenre(int genre) {
		this.genre = genre;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		ID3Tag tag = (ID3Tag)obj;
		return ((title == null && tag.getTitle() == null) || title.equals(tag.getTitle()))
				&& ((artist == null && tag.getArtist() == null) || artist.equals(tag.getArtist()))
				&& ((album == null && tag.getAlbum() == null) || album.equals(tag.getAlbum())) 
				&& (year == tag.getYear()) 
				&& ((comment == null && tag.getComment() == null) || comment.equals(tag.getComment()))
				&& (genre == tag.getGenre());
	}
	
	@Override
	public int hashCode()
	{
		return -1;
	}
	
	@Override
	public String toString() {
		StringBuffer mp3Tag = new StringBuffer();
		mp3Tag.append("Artist: ").append((artist == null ? "NULL" : artist)).append("\n");
		mp3Tag.append("Title: ").append((title == null ? "NULL" : title)).append("\n");
		mp3Tag.append("Album: ").append((album == null ? "NULL" : album)).append("\n");
		mp3Tag.append("Year: ").append((year == 0 ? "NULL" : year)).append("\n");
		mp3Tag.append("Comment: ").append((comment == null ? "NULL" : comment)).append("\n");
		mp3Tag.append("Genre: ").append((genre == 0 ? "NULL" : genre)).append("\n");

		return mp3Tag.toString();
	    }
	 
}