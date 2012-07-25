package upanel.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.swing.table.AbstractTableModel;

public class ProfileManager {

	// OUT
	private static FileOutputStream foS;
	private static BufferedOutputStream boS;
	private static ZipOutputStream zoS;
	private static ObjectOutputStream ooS;

	// IN
	private static FileInputStream fiS;
	private static BufferedInputStream biS;
	private static ZipInputStream ziS;
	private static ObjectInputStream oiS;
	private static ByteArrayOutputStream baoS;

	public static void loadProfile(File s) {// work

		try {
			fiS = new FileInputStream(s);
			biS = new BufferedInputStream(fiS);
			ziS = new ZipInputStream(biS);

			ziS.getNextEntry();

			if (ziS.getNextEntry().getName().equals("Profile")) {

				Preferences.importPreferences(ziS);
			}

			ziS.close();
			fiS.close();
			biS.close();

		} catch (final IOException e2) {
			// TODO: handle exception
		} catch (final InvalidPreferencesFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public static AbstractTableModel loadPattern(File s) {// WORK!

		Object modelTab = null;

		try {
			fiS = new FileInputStream(s);
			biS = new BufferedInputStream(fiS);
			ziS = new ZipInputStream(biS);

			if (ziS.getNextEntry().getName().equals("TablePatternModel")) {

				oiS = new ObjectInputStream(ziS);

				modelTab = oiS.readObject();

			}

			ziS.close();
			oiS.close();
			fiS.close();
			biS.close();

		} catch (final IOException e2) {
			// TODO: handle exception
		} catch (final ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return (AbstractTableModel) modelTab;
	}

	public static void load(File s, Preferences pref) {// WORK!

		try {
			fiS = new FileInputStream(s);
			biS = new BufferedInputStream(fiS);
			ziS = new ZipInputStream(biS);
			oiS = new ObjectInputStream(ziS);

			if (ziS.getNextEntry().getName().equals("TablePatternModel")) {

				oiS = new ObjectInputStream(ziS);

				oiS.readObject();

			}

			if (ziS.getNextEntry().getName().equals("Profile")) {

				Preferences.importPreferences(oiS);
			}

			oiS.close();
			ziS.close();
			fiS.close();
			biS.close();

		} catch (final IOException e2) {
			// TODO: handle exception
		} catch (final InvalidPreferencesFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (final ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public static void reSave(File s, Preferences prefs) {// change only

		// profile// work!!!

		Object tmpObj = null;

		// ZipEntry entry = null;
		try {
			fiS = new FileInputStream(s);
			biS = new BufferedInputStream(fiS);
			ziS = new ZipInputStream(biS);
			ziS.getNextEntry();
			oiS = new ObjectInputStream(ziS);

			tmpObj = oiS.readObject();

			ziS.close();
			fiS.close();
			biS.close();

			foS = new FileOutputStream(s);
			boS = new BufferedOutputStream(foS);
			zoS = new ZipOutputStream(boS);

			zoS.setMethod(ZipOutputStream.DEFLATED);
			zoS.setLevel(Deflater.BEST_COMPRESSION);

			zoS.putNextEntry(new ZipEntry("TablePatternModel"));
			ooS = new ObjectOutputStream(zoS);

			ooS.writeObject(tmpObj);

			zoS.putNextEntry(new ZipEntry("Profile"));

			prefs.exportSubtree(zoS);

			zoS.close();
			boS.close();
			foS.close();
			foS.close();
		} catch (final FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void reSave(File s, Object obj) {// change only object//WORK!!

		try {
			fiS = new FileInputStream(s);
			biS = new BufferedInputStream(fiS);
			ziS = new ZipInputStream(biS);

			baoS = new ByteArrayOutputStream();

			ziS.getNextEntry();

			ziS.getNextEntry();

			final byte buff[] = new byte[1024];

			int len;
			while ((len = ziS.read(buff)) != -1) {

				baoS.write(buff, 0, len);
			}

			ziS.close();
			// fiS.close();
			// biS.close();

			foS = new FileOutputStream(s);
			boS = new BufferedOutputStream(foS);// parametr
			zoS = new ZipOutputStream(boS);

			zoS.setMethod(ZipOutputStream.DEFLATED);
			zoS.setLevel(Deflater.BEST_COMPRESSION);

			zoS.putNextEntry(new ZipEntry("TablePatternModel"));

			ooS = new ObjectOutputStream(zoS);

			ooS.writeObject(obj);

			zoS.putNextEntry(new ZipEntry("Profile"));

			baoS.writeTo(zoS);

			zoS.close();
			// boS.close();
			// baoS.close();
			// foS.close();
		} catch (final FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void save(File s, AbstractTableModel obj, Preferences pref) {// WORK!!

		try {

			foS = new FileOutputStream(s);
			boS = new BufferedOutputStream(foS);
			zoS = new ZipOutputStream(boS);

			zoS.setMethod(ZipOutputStream.DEFLATED);
			zoS.setLevel(Deflater.BEST_COMPRESSION);

			zoS.putNextEntry(new ZipEntry("TablePatternModel"));
			ooS = new ObjectOutputStream(zoS);
			ooS.writeObject(obj);

			zoS.putNextEntry(new ZipEntry("Profile"));
			pref.exportSubtree(zoS);

			zoS.close();
			ooS.close();
			foS.close();
			boS.close();
		} catch (final FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (final IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (final BackingStoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public static void savePattern(File s, Object obj) {

		try {

			foS = new FileOutputStream(s);
			boS = new BufferedOutputStream(foS);
			zoS = new ZipOutputStream(boS);

			zoS.setMethod(ZipOutputStream.DEFLATED);
			zoS.setLevel(Deflater.BEST_COMPRESSION);

			zoS.putNextEntry(new ZipEntry("TablePatternModel"));
			ooS = new ObjectOutputStream(zoS);
			ooS.writeObject(obj);

			zoS.close();
			ooS.close();
			foS.close();
			boS.close();
		} catch (final FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (final IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public static void saveProfile(File s) {

		try {

			foS = new FileOutputStream(s);
			boS = new BufferedOutputStream(foS);
			zoS = new ZipOutputStream(boS);

			zoS.setMethod(ZipOutputStream.DEFLATED);
			zoS.setLevel(Deflater.BEST_COMPRESSION);

			zoS.putNextEntry(new ZipEntry("TablePatternModel"));

			zoS.putNextEntry(new ZipEntry("Profile"));
			Preferences.userNodeForPackage(ProfileManager.class).exportSubtree(zoS);

			zoS.close();
			foS.close();
			boS.close();
		} catch (final FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (final IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (final BackingStoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	private ProfileManager() {

	}
}