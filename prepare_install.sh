cat > /data/local/tmp/install_new.bsh << 'BSHEOF'
import java.io.*;
import android.content.pm.PackageInstaller;
import android.app.PendingIntent;
import android.content.Intent;

app = android.app.ActivityThread.currentApplication();
ctx = app.getApplicationContext();

// Read APK into memory
File apkFile = new File("/data/local/tmp/simpatik14-new.apk");
if (!apkFile.exists()) { println("ERROR: APK not found"); System.exit(1); }
long len = apkFile.length();
println("APK size: " + len + " bytes");

// Write to cache dir so session write can access it
byte[] apkData = new byte[(int)len];
FileInputStream fis = new FileInputStream(apkFile);
fis.read(apkData);
fis.close();

File tmpFile = new File(ctx.getCacheDir(), "install_simpatik.apk");
FileOutputStream fos = new FileOutputStream(tmpFile);
fos.write(apkData);
fos.close();
println("Written to: " + tmpFile.getAbsolutePath());

// PackageInstaller
pi = ctx.getPackageManager().getPackageInstaller();
sp = new PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL);
sp.setSize(tmpFile.length());
sp.setInstallReason(PackageInstaller.INSTALL_REASON_USER);

try {
    int sid = pi.createSession(sp);
    println("Session: " + sid);
    session = pi.openSession(sid);

    // Write
    out = session.openWrite("base.apk", 0, tmpFile.length());
    FileInputStream src = new FileInputStream(tmpFile);
    byte[] buf = new byte[65536];
    int cnt;
    while ((cnt = src.read(buf)) > 0) out.write(buf, 0, cnt);
    src.close();
    session.fsyncWrite(out);
    out.close();

    // Commit with immutable PendingIntent
    Intent intent = new Intent();
    PendingIntent pend = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_IMMUTABLE);
    session.commit(pend.getIntentSender());
    session.close();
    println("Commit sent! Check notification for result.");
} catch (e) {
    println("Error: " + e.toString());
    PrintWriter pw = new PrintWriter(System.out, true);
    e.printStackTrace(pw);
}
BSHEOF
echo "Script written"