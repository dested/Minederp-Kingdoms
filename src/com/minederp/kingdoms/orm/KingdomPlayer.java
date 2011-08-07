package com.minederp.kingdoms.orm;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

import com.minederp.kingdoms.KingdomsPlugin;

				public class KingdomPlayer {
				 
				
				public static KingdomPlayer makeKingdomPlayer (ResultSet st){ 
KingdomPlayer f=new KingdomPlayer();
try{
f.setKingdomPlayerID(st.getInt("KingdomPlayerID")  );
f.setPlayerName(st.getString("PlayerName")  );
f.setPlayerNickName(st.getString("PlayerNickName")  );
f.setKingdomID(st.getInt("KingdomID")  );
f.setTownID(st.getInt("TownID")  );

	} catch (SQLException e) {
			e.printStackTrace();
		}
return f;
						}private static List<KingdomPlayer> loopThroughKingdomPlayer (ResultSet st){ 
List<KingdomPlayer> fm=new ArrayList<KingdomPlayer>();
try{
while (st.next()) {
fm.add(makeKingdomPlayer(st));
}
	} catch (SQLException e) {
			e.printStackTrace();
		}
return fm;
}
								private int _KingdomPlayerID;
								public int getKingdomPlayerID(){ return _KingdomPlayerID; }
								public void setKingdomPlayerID(int aKingdomPlayerID){   _KingdomPlayerID=aKingdomPlayerID; }
								public static List<KingdomPlayer> getAllByKingdomPlayerID(int aKingdomPlayerID){ 

try{
return loopThroughKingdomPlayer(KingdomsPlugin.wrapper.selectQuery("*","KingdomPlayer","where KingdomPlayerID="+ aKingdomPlayerID+""));

} catch (SQLException e) {
			e.printStackTrace();
return null;	
}

}
								public static KingdomPlayer getFirstByKingdomPlayerID(int aKingdomPlayerID){ 
try{
ResultSet fc=KingdomsPlugin.wrapper.selectQuery("*","KingdomPlayer","where KingdomPlayerID="+ aKingdomPlayerID+"");
if(fc.next())
	return makeKingdomPlayer(fc);
} catch (SQLException e) {
			e.printStackTrace();
		}

return null;
}
								private String _PlayerName;
								public String getPlayerName(){ return _PlayerName; }
								public void setPlayerName(String aPlayerName){  if(aPlayerName==null || aPlayerName.equals("null")) return;  _PlayerName=aPlayerName; }
								public static List<KingdomPlayer> getAllByPlayerName(String aPlayerName){ 

try{
return loopThroughKingdomPlayer(KingdomsPlugin.wrapper.selectQuery("*","KingdomPlayer","where PlayerName='"+ aPlayerName+"'"));

} catch (SQLException e) {
			e.printStackTrace();
return null;	
}

}
								public static KingdomPlayer getFirstByPlayerName(String aPlayerName){ 
try{
ResultSet fc=KingdomsPlugin.wrapper.selectQuery("*","KingdomPlayer","where PlayerName='"+ aPlayerName+"'");
if(fc.next())
	return makeKingdomPlayer(fc);
} catch (SQLException e) {
			e.printStackTrace();
		}

return null;
}
								private String _PlayerNickName;
								public String getPlayerNickName(){ return _PlayerNickName; }
								public void setPlayerNickName(String aPlayerNickName){  if(aPlayerNickName==null || aPlayerNickName.equals("null")) return;  _PlayerNickName=aPlayerNickName; }
								public static List<KingdomPlayer> getAllByPlayerNickName(String aPlayerNickName){ 

try{
return loopThroughKingdomPlayer(KingdomsPlugin.wrapper.selectQuery("*","KingdomPlayer","where PlayerNickName='"+ aPlayerNickName+"'"));

} catch (SQLException e) {
			e.printStackTrace();
return null;	
}

}
								public static KingdomPlayer getFirstByPlayerNickName(String aPlayerNickName){ 
try{
ResultSet fc=KingdomsPlugin.wrapper.selectQuery("*","KingdomPlayer","where PlayerNickName='"+ aPlayerNickName+"'");
if(fc.next())
	return makeKingdomPlayer(fc);
} catch (SQLException e) {
			e.printStackTrace();
		}

return null;
}
								public Kingdom getKingdom(){ return Kingdom.getFirstByKingdomID(_KingdomID); }
								private int _KingdomID;
								public int getKingdomID(){ return _KingdomID; }
								public void setKingdomID(int aKingdomID){   _KingdomID=aKingdomID; }
								public static List<KingdomPlayer> getAllByKingdomID(int aKingdomID){ 

try{
return loopThroughKingdomPlayer(KingdomsPlugin.wrapper.selectQuery("*","KingdomPlayer","where KingdomID="+ aKingdomID+""));

} catch (SQLException e) {
			e.printStackTrace();
return null;	
}

}
								public static KingdomPlayer getFirstByKingdomID(int aKingdomID){ 
try{
ResultSet fc=KingdomsPlugin.wrapper.selectQuery("*","KingdomPlayer","where KingdomID="+ aKingdomID+"");
if(fc.next())
	return makeKingdomPlayer(fc);
} catch (SQLException e) {
			e.printStackTrace();
		}

return null;
}
								public Town getTown(){ return Town.getFirstByTownID(_TownID); }
								private int _TownID;
								public int getTownID(){ return _TownID; }
								public void setTownID(int aTownID){   _TownID=aTownID; }
								public static List<KingdomPlayer> getAllByTownID(int aTownID){ 

try{
return loopThroughKingdomPlayer(KingdomsPlugin.wrapper.selectQuery("*","KingdomPlayer","where TownID="+ aTownID+""));

} catch (SQLException e) {
			e.printStackTrace();
return null;	
}

}
								public static KingdomPlayer getFirstByTownID(int aTownID){ 
try{
ResultSet fc=KingdomsPlugin.wrapper.selectQuery("*","KingdomPlayer","where TownID="+ aTownID+"");
if(fc.next())
	return makeKingdomPlayer(fc);
} catch (SQLException e) {
			e.printStackTrace();
		}

return null;
}
								public static List<KingdomPlayer> getAll(){ 
try{
return loopThroughKingdomPlayer(KingdomsPlugin.wrapper.selectQuery("*","KingdomPlayer",""));} catch (SQLException e) {
			e.printStackTrace();
		return null;}
}
								public void insert(){try{
KingdomsPlugin.wrapper.insertQuery("KingdomPlayer","default,'"+ _PlayerName+"', '"+ _PlayerNickName+"', "+ _KingdomID+", "+ _TownID+""  );

} catch (SQLException e) {
			e.printStackTrace();
		}
}
								public void update(){ 
try{
KingdomsPlugin.wrapper.updateQuery("UPDATE KingdomPlayer SET PlayerName= '"+ _PlayerName+"' , PlayerNickName= '"+ _PlayerNickName+"' , KingdomID= "+ _KingdomID+" , TownID= "+ _TownID+" where KingdomPlayerID="+_KingdomPlayerID+" ");
} catch (SQLException e) {
			e.printStackTrace();
		}
}
	@Override
	public boolean equals(Object b) {
        if(b instanceof KingdomPlayer)
            if(((KingdomPlayer)b)._KingdomPlayerID==_KingdomPlayerID)
                return true;
		return false;

	}
								public void delete(){ 
try{
KingdomsPlugin.wrapper.deleteQuery("KingdomPlayer","KingdomPlayerID = " + _KingdomPlayerID);
} catch (SQLException e) {
			e.printStackTrace();
		}
}}