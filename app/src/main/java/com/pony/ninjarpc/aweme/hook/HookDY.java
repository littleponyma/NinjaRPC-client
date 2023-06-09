package com.pony.ninjarpc.aweme.hook;

import com.pony.android.robust.IRobustLoadPkg;
import com.pony.android.robust.RBMethodFork;
import com.pony.android.robust.Robust;
import com.pony.android.robust.callbacks.RBLoadPkg;
import com.pony.ninjarpc.aweme.controller.CollectorController;
import com.pony.ninjarpc.aweme.utils.VLog;

public class HookDY implements IRobustLoadPkg {

    public static Object msManager = null;
    public static RBLoadPkg.LoadPkgParam mLpparam;

    @Override
    public void handleLoadPackage(RBLoadPkg.LoadPkgParam lpparam) {
        if (lpparam.processName.equals("com.ss.android.ugc.aweme")) {
            if (!lpparam.processName.equals(lpparam.packageName)) {
                return;
            }
            VLog.e("hello aweme " + lpparam.processName);
            CollectorController.getInstance().start();
            try {
                Robust.findRobustConstructor("com.bytedance.mobsec.metasec.ml.MSManager",
                        lpparam.classLoader, "ms.bd.c.m1$a", new RBMethodFork() {
                            @Override
                            protected void originInvokedMethod(MethodParam param) throws Throwable {
                                super.originInvokedMethod(param);
                                VLog.e("invoke msmanager  " + param.thisObject);
                                msManager = param.thisObject;
                                mLpparam = lpparam;
//                            String url = "https://webcast.huoshan.com/webcast/room/enter/?luckydog_base=kL61JjwrA3bjhwtQjrwT961MjbksURDc0Uq888cLt9ncF8mB9SXVBLWXcuLKGO-uGa6vrg000crQEff5MJwC2PtmgiTh3j-NZYvMgU-89zoJhurqVmhh3gn6uBlK3XGa1nhivWaJ19Plgpik_9j3mgVj6435M6FPpM8b-86akxk&luckydog_token=vGwG3eItLthTKN36zGZGW5WUJYz3bVfs6C2AeoR9YSL4Vg18kn_A_ibtUrvmh3_gbQx1aWaASOmmwf7p5x2qCA&luckydog_data=tVrggETFyXUdT7OrpLX1rg&webcast_sdk_version=2730&webcast_language=zh&webcast_locale=zh_CN&webcast_gps_access=2&current_network_quality_info=%7B%7D&is_pad=false&is_android_pad=0&is_landscape=false&carrier_region=CN&live_sdk_version=150300&need_personal_recommend=1&iid=1269275336119278&device_id=2905346602711534&ac=wifi&channel=360_1112_0322&aid=1112&app_name=live_stream&version_code=150300&version_name=15.3.0&device_platform=android&os=android&ssmix=a&device_type=2203121C&device_brand=Xiaomi&language=zh&os_api=28&os_version=9&manifest_version_code=150300&resolution=1080*1920&dpi=280&update_version_code=15030003&_rticket=1684314321355&tab_mode=3&client_version_code=150300&mcc_mnc=46000&hs_location_permission=0&cpu_support64=true&host_abi=armeabi-v7a&rom_version=XIAOMI_unknown&cdid=4866ebb5-c3ff-4549-815f-bd9bd5956da3&new_nav=0&screen_width=617&ws_status=ConnectionState%7BState%3D1%7D&settings_version=24&last_update_time=1684314315741&cpu_model=placeholder&ts=1684314321";
//                            Robust.invokeMethod(HookHS.msManager, "setDeviceID", "2905346602711534");
//                            Robust.invokeMethod(HookHS.msManager, "setInstallID", "1269275336119278");
//                            Class<?> NetworkParams = Robust.findClass("com.bytedance.frameworks.baselib.network.http.NetworkParams", lpparam.classLoader);
//                            HashMap<String, List<String>> map = new HashMap<>();
//                            map.put("accept-encoding", Arrays.asList("gzip"));
//                            map.put("passport-sdk-version", Arrays.asList("30842"));
//                            map.put("room-enter-user-login-ab", Arrays.asList("0"));
//                            map.put("user-agent", Arrays.asList("com.ss.android.ugc.live/150300 (Linux; U; Android 9; zh_CN; 2203121C; Build/PQ3B.190801.002;tt-ok/3.10.0.2)"));
//                            map.put("sdk-version", Arrays.asList("2"));
//                            map.put("x-ss-stub", Arrays.asList("1908D20BF6EDE89CBB15A01468FC65D6"));
//                            map.put("x-vc-bdturing-sdk-version", Arrays.asList("3.1.0.cn"));
//                            map.put("x-webcast-tag", Arrays.asList("s1:enter_room"));
//                            Map sign = (Map) Robust.invokeStaticMethod(NetworkParams,
//                                    "tryAddSecurityFactor", url, map);
//                            VLog.e(JSON.toJSONString(sign));

                            }
                        });
            } catch (Exception e) {

            }


        }
    }


}
