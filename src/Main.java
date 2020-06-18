import cn.kuwo.base.utils.a.d;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main
{
    private static String first = "ylzsxkwm";
    
	public static void main(String[] args) throws Exception
	{
        Scanner in = new Scanner(System.in);
		searchMusic(in.next());
	}
    
    private static void downloadUrl(String id) {
        try {
            URL url = new URL("http://nmobi.kuwo.cn/mobi.s?f=kuwo&q=" + encode(id));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept-Encoding", "gzip");
            conn.setRequestProperty("Host", "nmobi.kuwo.cn");
            conn.setRequestProperty("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 9; vivo X21i Build/P00610)");
            conn.connect();
            String message = "";
            if (conn.getResponseCode() == 200) {
                InputStream inputStream = conn.getInputStream();
                byte[] data = new byte[1024];
                StringBuffer sb1 = new StringBuffer();
                int length = 0;
                while ((length = inputStream.read(data)) != -1) {
                    String s = new String(data, 0,length);
                    sb1.append(s);
                }
                message=sb1.toString();
                inputStream.close();
            }
            conn.disconnect();
            System.out.println(new String(message.getBytes(), "utf-8"));
        }catch(Exception e) {
            
        }
    }
    
    private static String encode(String id) {
        String s = "user=e3cc098fd4c59ce2&android_id=e3cc098fd4c59ce2&prod=kwplayer_ar_9.3.1.3&corp=kuwo&newver=2&vipver=9.3.1.3&source=kwplayer_ar_9.3.1.3_qq.apk&p2p=1&notrace=0&type=convert_url2&br=2000kflac&format=flac|mp3|aac&sig=0&rid="+ id +"&priority=bitrate&loginUid=435947810&network=WIFI&loginSid=1694167478&mode=download&uid=658048466";
        byte[] bArr = s.getBytes();
        byte[] a2 = d.a(bArr, bArr.length, first.getBytes(), first.getBytes().length);
        return new String(cn.kuwo.base.utils.a.b.a(a2, a2.length));
    }
    
    private static void searchMusic(String keyword) throws Exception {
        String key = URLEncoder.encode(keyword, "UTF-8");
        String crsf = getCrsf();
        String[] nn = crsf.split("; ");
        if (nn == null) {
            System.out.println("程序异常");
            return;
        }
        String crsfS = nn[0].substring(9);
        URL url = new URL("http://www.kuwo.cn/api/www/search/searchMusicBykeyWord?key=" + key +"&pn=1&rn=30&reqId=");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Host", "www.kuwo.cn");
        connection.setRequestProperty("Referer", "http://www.kuwo.cn/");
        connection.setRequestProperty("csrf", crsfS);
        connection.setRequestProperty("Cookie", crsf);
        connection.setRequestProperty("Accept", "application/json, text/plain, */*");
        connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.122 Safari/537.36");
        connection.setRequestMethod("GET");
        connection.connect();
        String message = "";
        if (connection.getResponseCode() == 200) {
            InputStream inputStream=connection.getInputStream();
            byte[] data=new byte[1024];
            StringBuffer sb1=new StringBuffer();
            int length=0;
            while ((length=inputStream.read(data))!=-1){
                String s=new String(data, 0,length);
                sb1.append(s);
            }
            message=sb1.toString();
            inputStream.close();

        }
        connection.disconnect();
        JSONObject json = new JSONObject(message);
        String data = json.getString("data");
        json = new JSONObject(data);
        JSONArray list = json.getJSONArray("list");
        for(int i = 0; i < list.length(); i++) {
            json = list.getJSONObject(i);
            String title = json.getString("name");
            String author = json.getString("artist");
            String album = json.getString("duration");
            String rid = json.getString("rid");
            System.out.println("===歌曲信息===");
            System.out.println("歌曲名字：" + title + " 作者：" + author + " 时间：" + album + " 歌曲id：" + rid);
            System.out.println();
        }

        System.out.println("输入想下载的歌曲id");
        Scanner in = new Scanner(System.in);
        String id = in.next();
        downloadUrl(id);
    }

    private static String getCrsf() throws Exception {
        URL url = new URL("http://www.kuwo.cn/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Host", "www.kuwo.cn");
        connection.setRequestProperty("Referer", "http://www.kuwo.cn/");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.122 Safari/537.36");
        connection.setRequestMethod("GET");
        connection.connect();
        return connection.getHeaderField("Set-Cookie");
	}
}
