package sharemyscreen.sharemyscreen.DAO;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

import sharemyscreen.sharemyscreen.Entities.ProfileEntity;

/**
 * Created by roucou-c on 09/12/15.
 */
public class ProfileManager extends ProfileDAO{

    public ProfileManager(Context pContext) {
        super(pContext);
    }

    public long add(ProfileEntity profileEntity) {
        if (profileEntity !=  null && profileEntity.get_username() != null) {
            ProfileEntity newProfile = this.selectByUsername(profileEntity.get_username());
            if (newProfile != null) {
                profileEntity.set_id(newProfile.get_id());
                super.modify(profileEntity);
                return newProfile.get_id();
            }
            else {
                return super.add(profileEntity);
            }
        }
        return 0;
    }

    public void modifyProfil(ProfileEntity profile) {
        this.modify(profile);
    }

//    public ProfileEntity modifyProfil(String username, String password) {
//
//        ProfileEntity profile = this.selectByUsername(username);
//
//        if (profile == null) {
//            long id = this.add(new ProfileEntity(username, password));
//            return this.selectById(id);
//        }
//        else {
//            profile.set_password(password);
//            this.modify(profile);
//            return profile;
//        }
//    }
//
//    public void modifyProfil(HashMap<String, String> mapProfile) {
//        String username = mapProfile.get("username");
//        if (username != null) {
//            ProfileEntity profile = this.selectByUsername(username);
//            if (profile != null) {
//                profile.update(mapProfile);
//                this.modify(profile);
//            }
//        }
//
//    }
}
