package iotdf.iotgateway.DeviceFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iotdf.iotgateway.DevicePanel;
import iotdf.iotgateway.R;


public class Control extends Fragment implements View.OnClickListener,InputFrag.inputListener {
    ListView clistView;
    FragmentCallback fragmentCallback=null;
    private View view;
    String postion;
    String arduinoNum;
    String Inputvalue="0";
    boolean Switcher=true;//true时电机正转，否则反转
    int SwitchCounter=1;
    int INSwithcher=1;
    int DESwithcher=1;
    int Dialog_increase=1;
    int Dialog_decrease=2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle=getArguments();
        postion=bundle.getString("Position");
        final String[] postionInfo=postion.split(",");
        arduinoNum=postionInfo[1].replaceAll("\\D","");//传感节点编号,过滤掉所有非数字字符
        switch(arduinoNum){
            case "0":arduinoNum="000";break;
            case "1":arduinoNum="001";break;
            case "2":arduinoNum="010";break;
            case "3":arduinoNum="011";break;
            case "4":arduinoNum="100";break;
            case "5":arduinoNum="101";break;
            case "6":arduinoNum="110";break;
            case "7":arduinoNum="111";break;
        }
        System.out.println("测试："+mathhelper.str2HexStr("10101000000000000011111001111011"));
        if(view==null)
        view = inflater.inflate(R.layout.fragment_control, null);
        clistView = (ListView)view.findViewById(R.id.controlList);
        SimpleAdapter adapter = new SimpleAdapter(view.getContext(),getData(),R.layout.control_list,
                new String[]{"option","img"},
                new int[]{R.id.option,R.id.img});
        clistView.setAdapter(adapter);
        clistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                    fragmentCallback.CallBack(mathhelper.str2HexStr("101010"+arduinoNum+"00100000000000000001011"));
                        System.out.println("发送："+mathhelper.str2HexStr("101010"+arduinoNum+"00100000000000000001011"));
                        break;
                    case 1:
                        switch (SwitchCounter){
                            case 1:fragmentCallback.CallBack(mathhelper.str2HexStr("101010"+arduinoNum+"00000000000000010101011"));
                                System.out.println("发送："+mathhelper.str2HexStr("101010"+arduinoNum+"00000000000000010101011"));
                                Toast toast1=Toast.makeText(getActivity(),"接收间隔：十分钟",Toast.LENGTH_SHORT);
                                toast1.show();
                                SwitchCounter=2;
                                break;
                            case 2:fragmentCallback.CallBack(mathhelper.str2HexStr("101010"+arduinoNum+"00000000000000111101011"));
                                System.out.println("发送："+mathhelper.str2HexStr("101010"+arduinoNum+"00000000000000111101011"));
                                Toast toast2=Toast.makeText(getActivity(),"接收间隔：三十分钟",Toast.LENGTH_SHORT);
                                toast2.show();
                                SwitchCounter=3;
                                break;
                            default:fragmentCallback.CallBack(mathhelper.str2HexStr("101010"+arduinoNum+"00000000000001111001011"));
                                System.out.println("发送："+mathhelper.str2HexStr("101010"+arduinoNum+"00000000000001111001011"));
                                Toast toast3=Toast.makeText(getActivity(),"接收间隔：一小时",Toast.LENGTH_SHORT);
                                toast3.show();
                                SwitchCounter=1;
                                break;
                        }
                        break;

                    case 2:if(Switcher){
                        fragmentCallback.CallBack(mathhelper.str2HexStr("101001"+arduinoNum+"01000000000000000001011"));
                        System.out.println("发送："+mathhelper.str2HexStr("101001"+arduinoNum+"01000000000000000001011"));
                        Toast toast3=Toast.makeText(getActivity(),"电机反转",Toast.LENGTH_SHORT);
                        toast3.show();
                        Switcher=false;
                    }
                        else
                    {
                        fragmentCallback.CallBack(mathhelper.str2HexStr("101001"+arduinoNum+"01000000000000000011011"));
                        System.out.println("发送："+mathhelper.str2HexStr("101001"+arduinoNum+"01000000000000000011011"));
                        Toast toast3=Toast.makeText(getActivity(),"电机正转",Toast.LENGTH_SHORT);
                        toast3.show();
                        Switcher=true;
                    }
                        break;
                    case 3:
                        if(postion.contains("光照"))
                        switch (INSwithcher){
                            case 1:
                                fragmentCallback.CallBack(mathhelper.str2HexStr("101001"+arduinoNum+"00000000000000000001011"));
                                System.out.println("发送："+mathhelper.str2HexStr("101001"+arduinoNum+"00000000000000000001011"));
                                Toast toast1=Toast.makeText(getActivity(),"光照档位：1",Toast.LENGTH_SHORT);
                                toast1.show();
                                INSwithcher=2;
                                break;
                            case 2:
                                fragmentCallback.CallBack(mathhelper.str2HexStr("101001"+arduinoNum+"00000000000111110101011"));
                                System.out.println("发送："+mathhelper.str2HexStr("101001"+arduinoNum+"00000000000111110101011"));
                                Toast toast2=Toast.makeText(getActivity(),"光照档位：2",Toast.LENGTH_SHORT);
                                toast2.show();
                                INSwithcher=3;
                                break;
                            case 3:
                                fragmentCallback.CallBack(mathhelper.str2HexStr("101001"+arduinoNum+"00000000001111101001011"));
                                System.out.println("发送："+mathhelper.str2HexStr("101001"+arduinoNum+"00000000001111101001011"));
                                Toast toast3=Toast.makeText(getActivity(),"光照档位：3",Toast.LENGTH_SHORT);
                                toast3.show();
                                INSwithcher=4;
                                break;
                            case 4:
                                fragmentCallback.CallBack(mathhelper.str2HexStr("101001"+arduinoNum+"00000000010111011101011"));
                                System.out.println("发送："+mathhelper.str2HexStr("101001"+arduinoNum+"00000000010111011101011"));
                                Toast toast4=Toast.makeText(getActivity(),"光照档位：4",Toast.LENGTH_SHORT);
                                toast4.show();
                                INSwithcher=5;
                                break;
                            case 5:
                                fragmentCallback.CallBack(mathhelper.str2HexStr("101001"+arduinoNum+"00000000011111001111011"));
                                System.out.println("发送："+mathhelper.str2HexStr("101001"+arduinoNum+"00000000011111001111011"));
                                Toast toast5=Toast.makeText(getActivity(),"光照档位：5",Toast.LENGTH_SHORT);
                                toast5.show();
                                INSwithcher=1;
                                break;
                        }
                        else
                        {
                            /*fragmentCallback.CallBack(mathhelper.str2HexStr("101001"+arduinoNum+"00100000000001111001011"));
                            System.out.println("发送："+mathhelper.str2HexStr("101001"+arduinoNum+"00100000000001111001011"));*/
                            fragmentCallback.CallBack(mathhelper.str2HexStr("101001"+arduinoNum+"00100000000011001001011"));
                            System.out.println("发送："+mathhelper.str2HexStr("101001"+arduinoNum+"00100000000011001001011"));
                            Toast toast=Toast.makeText(getActivity(),"加水",Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        break;
                    case 4:switch (INSwithcher){
                        case 3:
                            fragmentCallback.CallBack(mathhelper.str2HexStr("101001"+arduinoNum+"00000000000000000001011"));
                            System.out.println("发送："+mathhelper.str2HexStr("101001"+arduinoNum+"00000000000000000001011"));
                            Toast toast1=Toast.makeText(getActivity(),"光照档位：1",Toast.LENGTH_SHORT);
                            toast1.show();
                            INSwithcher=2;
                            break;
                        case 4:
                            fragmentCallback.CallBack(mathhelper.str2HexStr("101001"+arduinoNum+"00000000000111110101011"));
                            System.out.println("发送："+mathhelper.str2HexStr("101001"+arduinoNum+"00000000000111110101011"));
                            Toast toast2=Toast.makeText(getActivity(),"光照档位：2",Toast.LENGTH_SHORT);
                            toast2.show();
                            INSwithcher=3;
                            break;
                        case 5:
                            fragmentCallback.CallBack(mathhelper.str2HexStr("101001"+arduinoNum+"00000000001111101001011"));
                            System.out.println("发送："+mathhelper.str2HexStr("101001"+arduinoNum+"00000000001111101001011"));
                            Toast toast3=Toast.makeText(getActivity(),"光照档位：3",Toast.LENGTH_SHORT);
                            toast3.show();
                            INSwithcher=4;
                            break;
                        case 1:
                            fragmentCallback.CallBack(mathhelper.str2HexStr("101001"+arduinoNum+"00000000010111011101011"));
                            System.out.println("发送："+mathhelper.str2HexStr("101001"+arduinoNum+"00000000010111011101011"));
                            Toast toast4=Toast.makeText(getActivity(),"光照档位：4",Toast.LENGTH_SHORT);
                            toast4.show();
                            INSwithcher=5;
                            break;
                        case 2:
                            fragmentCallback.CallBack(mathhelper.str2HexStr("101001"+arduinoNum+"00000000011111001111011"));
                            System.out.println("发送："+mathhelper.str2HexStr("101001"+arduinoNum+"00000000011111001111011"));
                            Toast toast5=Toast.makeText(getActivity(),"光照档位：5",Toast.LENGTH_SHORT);
                            toast5.show();
                            INSwithcher=1;
                            break;
                    }
                        break;
                }
            }
        });
        return view;
    }


    @Override
    public void onDestroyView(){
        super.onDestroyView();
        ((ViewGroup)view.getParent()).removeView(view);
    }

    public static Fragment newInstance(String arg){
        Control fragment = new Control();
        Bundle bundle = new Bundle();
        bundle.putString("Positon", arg);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentCallback=(DevicePanel)getActivity();
    }

    @Override
    public void onClick(View view) {

    }
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("option", "Reboot");
        map.put("img", R.drawable.con_reboot);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("option", "Config");
        map.put("img", R.drawable.con_config);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("option", "Curtain");
        map.put("img", R.drawable.con_curtain);
        list.add(map);

        if(postion.contains("水份")||postion.contains("光照"))
        {
            map = new HashMap<String, Object>();
            map.put("option", "Increase");
            map.put("img", R.drawable.con_up);
            list.add(map);
            if(postion.contains("光照")){
            map = new HashMap<String, Object>();
            map.put("option", "Decrease");
            map.put("img", R.drawable.con_down);
            list.add(map);
            }
        }

        return list;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void onInputComplete(String inputValue) {
        this.Inputvalue=inputValue;
    }
}
class mathhelper
{
    public static String str2HexStr(String integer) {
        StringBuffer integerSum = new StringBuffer();
        int loop = 0; // 循环次数
        if (integer.length() % 4 == 0) {
            loop = integer.length() / 4;
        } else {
            loop = integer.length() / 4 + 1;
        }
        String binary = "";
        for (int i = 1; i <= loop; i++) {
            if (i != loop) {
                binary = integer.substring(integer.length() - i * 4,
                        integer.length() - i * 4 + 4);
            } else {
                binary = mathhelper.appendZero(
                        integer.substring(0, integer.length() - (i - 1) * 4),
                        4, true);
            }
            integerSum.append(mathhelper.toHex(String.valueOf(mathhelper
                    .binaryIntToDecimalis(binary))));
        }
        return integerSum.reverse().toString();
    }
    public static String appendZero(String str, int len, boolean flag) {
        String zero = "0";
        if (null == str || str.length() == 0) {
            return "";
        }
        if (str.length() >= len) {
            return str;
        }
        for (int i = str.length(); i < len; i++) {
            if (flag) {
                str = zero + str;
            } else {
                str += zero;
            }
        }
        return str;
    }
    public static String toHex(String hex) {
        String str = "";
        switch(Integer.parseInt(hex)){
            case 10 : str = "a"; break;
            case 11 : str = "b"; break;
            case 12 : str = "c"; break;
            case 13 : str = "d"; break;
            case 14 : str = "e"; break;
            case 15 : str = "f"; break;
            default : str = hex;
        }
        return str;
    }
    public static int binaryIntToDecimalis(String inteter) {
    int inteterSum = 0;
    for (int i = inteter.length(); i > 0; i--) {
        int scale = 2;
        if (inteter.charAt(-(i - inteter.length())) == '1') {
            if (i != 1) {
                for (int j = 1; j < i - 1; j++) {
                    scale *= 2;
                }
            } else {
                scale = 1;
            }
        } else {
            scale = 0;
        }
        inteterSum += scale;
    }
    return inteterSum;
}
 /*   public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }
 */
}