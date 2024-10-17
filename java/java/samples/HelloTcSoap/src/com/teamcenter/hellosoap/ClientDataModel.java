//==================================================
// 
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.hellosoap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TimeZone;

import com.teamcenter.schemas.soa._2006_03.base.ModelObject;
import com.teamcenter.schemas.soa._2006_03.base.Property;
import com.teamcenter.schemas.soa._2006_03.base.PropertyValue;
import com.teamcenter.schemas.soa._2006_03.base.RefId;
import com.teamcenter.schemas.soa._2006_03.base.ServiceData;

/**
 * This class helps manage the data returned for service requests.
 * WSDL-DIFFERENCE - Model Manager
 * All of the functionality in this class is provided in the Teamcenter
 * client bindings' ModelManager class and Data Model class instances (ItemRevision .etc).
 *
 */
public class ClientDataModel
{
    static       HashMap<String,ModelObject>        clientModel = new HashMap<String,ModelObject> ();
    static final String nullString;
    static final java.util.Calendar nulldate  = new GregorianCalendar();
    static final SimpleDateFormat wireFormat  = new SimpleDateFormat( "M/d/yyyy h:mm a");
    static final SimpleDateFormat xsdFormat   = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssZ");
    static
    {
        wireFormat.setTimeZone  (TimeZone.getTimeZone("GMT"));
        nulldate.setTimeZone    (TimeZone.getTimeZone("GMT"));

        nulldate.clear();
        nulldate.set(1, 0, 1, 0, 0, 0);  // "1/1/0001 12:00 AM";
        nullString = wireFormat.format(nulldate.getTime());
    }



    /**
     * Adds all of the ModelObjects found in the ServiceData to the client cache.
     * This client cache is a simple HashMap of UID/ModelObjects.
     * For ModelObjects that already exist in the cache, property values are merged
     * so the cached ModelObject will have the latest property values returned from
     * the server.
     *
     * WSDL-DIFFERENCE - Model Manager
     * The Teamcenter client bindings use the ModelManager (part of
     * the Connection object) to cache all instances ModelObjects returned
     * form any service. A WSDL binding treats each service request as
     * independent operations, and will only hold on to the returned
     * ModelObjects as long the client application keeps the returned
     * data structure.
     * Here an explicit call to the AppXClientDataModel will cache the
     * returned ModelObjects and merge property values with instances
     * already stored in the client.
     *
     * @param sd    ServiceData object returned from a service request.
     */
    public static void addObjects( ServiceData sd )
    {
        ModelObject[] allObjs = sd.getDataObjects();
        if(allObjs == null)
            return;


        for (int i = 0; i < allObjs.length; i++)
        {
            ModelObject obj = clientModel.get(allObjs[i].getUid());
            if(obj != null)
            {
                // If we already have this object in the cache
                // merge the existing property values with the newly returned values
                Property[] oldProperties = obj.getProperties();
                Property[] newProperties = allObjs[i].getProperties();
                HashMap<String,Property> tgtProperties = new HashMap<String,Property>();
                if(oldProperties != null)
                {
                    for (int j = 0; j < oldProperties.length; j++)
                    {
                        tgtProperties.put(oldProperties[j].getName(), oldProperties[j]);
                    }
                }
                if(newProperties != null)
                {
                    for (int j = 0; j < newProperties.length; j++)
                    {
                        tgtProperties.put(newProperties[j].getName(), newProperties[j]);
                    }
                }
                Property[] mergedProps = new Property[tgtProperties.size()];
                int k = 0;
                for (Iterator<String> iter = tgtProperties.keySet().iterator(); iter.hasNext();)
                {
                    mergedProps[k++] = tgtProperties.get( iter.next());
                }

                // Update the Property vector on the object
                obj.setProperties( mergedProps);
            }
            else
            {
                // New Object to the map
                clientModel.put(allObjs[i].getUid(), allObjs[i]);
            }
        }
    }

    /**
     * Return a ModelObject instance from the cache based on the RefId.
     * The Updated, Created, .etc vectors on the ServiceData are of RefId (UID),
     * Find the ModelObject in the cache based on the RefId. This method assumes that
     * the ServiceData was first added to the cache with the addObjects method.
     *
     * @param ids   List of UIDs
     * @return      Instance of the ModelObject
     */
    public static ModelObject[] getObjects( RefId[] ids)
    {
        ModelObject[] objects = new ModelObject[ids.length];
        for (int i = 0; i < ids.length; i++)
        {
            objects[i] = clientModel.get(ids[i].getUid());
        }
        return objects;
    }

    /**
     * Return a ModelObject instance from the cache based on a secondary instance.
     * This method assumes that the ServiceData was first added to the cache with the addObjects method.
     *
     * WSDL-DIFFERENCE - Model Manager
     * In a service response structure there may be multiple instances of
     * a single ModelObject. To minimize the size of the XML document,
     * these secondary instances (LoginResponse.getUser) only contain
     * basic information, UID & Type. The primary instance of the ModelObject
     * is always returned in the ServiceData (LoginResponse.getServicData).
     * The primary instance has all of the property values for the given
     * object.
     * In the Teamcenter client bindings the ModelManager will replace
     * all of these secondary instances with pointers to the primary
     * instance in ServiceData, all of which are cached in the Client
     * Data Model. With a WSDL client binding the client application is
     * forced to correlate the secondary instances with the primary
     * instance in the ServiceData.
     * The call below will find the fully populated instance of the User
     * ModelObject in the cache. This will only work if the corresponding
     * ServiceData has been previously added to the cache (AppXClientDataModel.addObjects)
     *
     *
     * @param ids   List of UIDs
     * @return      Instance of the ModelObject
     */
    public static ModelObject getObjectWithProperties( ModelObject ref )
    {
        ModelObject obj = clientModel.get(ref.getUid());
        if(obj == null)
            return ref;
        return obj;
    }

    /**
     * Return the value for the named property.
     *
     * @param obj   The ModelObject to pull the property from
     * @param name  Name of the desired property
     * @return      The value of the property
     *
     * @throws NotLoadedException   If the named property does not exist on the ModelObject.
     * This does not distinguish between a property that simply has not been downloaded, or
     * a invalid property name (one not defined in the Teamcenter Business Model).
     */
    public static String getPropertyStringValue( ModelObject obj, String name )
    throws NotLoadedException
    {
        String[] values = getPropertyStringValues( obj, name );
        return values[0];
    }

    /**
     * Return the values for the named property.
     *
     * @param obj   The ModelObject to pull the property from
     * @param name  Name of the desired property
     * @return      An array of property values
     *
     * @throws NotLoadedException   If the named property does not exist on the ModelObject.
     * This does not distinguish between a property that simply has not been downloaded, or
     * a invalid property name (one not defined in the Teamcenter Business Model).
     */
    public static String[] getPropertyStringValues( ModelObject obj, String name )
    throws NotLoadedException
    {
        Property[] properties = obj.getProperties();
        if(properties == null)
            throw new NotLoadedException( name );

        // Loop through the list of properties until we find the name that matches
        for(int i=0;i<properties.length; i++)
        {
            if(properties[i].getName().equals(name))
            {
                PropertyValue[] values = properties[i].getValues();
                String[] vals = new String[values.length];
                for(int j=0; j<values.length; j++)
                    vals[j]= values[j].getValue();

                // Return the values as-is
                return vals;
            }
        }

        // Ethier this property has not been download or the name is not valid
        throw new NotLoadedException( name );
    }


    /**
     * Return the ModelObject instance for the named property.
     * All properties are stored as plain strings on the ModelObject, properties that
     * represent a referenced ModelObject have UIDs for the value. This method
     * will use the UID to look up the ModelObject in the cache.  This method assumes
     * that the ServiceData was first added to the cache with the addObjects method.

     * @param obj   The ModelObject to pull the property from
     * @param name  Name of the desired property
     * @return      The ModelObject instance of the property
     *
     * @throws NotLoadedException   If the named property does not exist on the ModelObject.
     * This does not distinguish between a property that simply has not been downloaded, or
     * a invalid property name (one not defined in the Teamcenter Business Model).
     */
    public static ModelObject getPropertyModelObjectValue( ModelObject obj, String name )
    throws NotLoadedException
    {
        ModelObject[] allObjs = getPropertyModelObjectValues( obj, name );
        return allObjs[0];
    }

    /**
     * Return an array of ModelObject instances for the named property.
     * All properties are stored as plain strings on the ModelObject, properties that
     * represent a referenced ModelObject have UIDs for the value. This method
     * will use the UID to look up the ModelObject in the cache.  This method assumes
     * that the ServiceData was first added to the cache with the addObjects method.

     * @param obj   The ModelObject to pull the property from
     * @param name  Name of the desired property
     * @return      The array of ModelObject instances of the property
     *
     * @throws NotLoadedException   If the named property does not exist on the ModelObject.
     * This does not distinguish between a property that simply has not been downloaded, or
     * a invalid property name (one not defined in the Teamcenter Business Model).
     */
   public static ModelObject[] getPropertyModelObjectValues( ModelObject obj, String name )
    throws NotLoadedException
    {
        String[] uids = getPropertyStringValues( obj, name );
        ModelObject[] objs = new ModelObject[uids.length];
        for (int i = 0; i < objs.length; i++)
        {
            objs[i] = clientModel.get(uids[i]);
        }
        return objs;
    }

   /**
    * Return a Calendar instance for the named property.
    * All properties are stored as plain strings on the ModelObject, properties that
    * represent dates are store as xsd:dateTime syntax. This method will convert the
    * date string to a Calendar instance.

    * @param obj   The ModelObject to pull the property from
    * @param name  Name of the desired property
    * @return      The Calendar instances of the property
    *
    * @throws NotLoadedException   If the named property does not exist on the ModelObject.
    * This does not distinguish between a property that simply has not been downloaded, or
    * a invalid property name (one not defined in the Teamcenter Business Model).
    */
   public static Calendar getPropertyDateValue( ModelObject obj, String name )
    throws NotLoadedException
    {
        Calendar[] allDates = getPropertyDateValues( obj, name );
        return allDates[0];
    }


   /**
    * Return an array of Calendar instance for the named property.
    * All properties are stored as plain strings on the ModelObject, properties that
    * represent dates are store as xsd:dateTime syntax. This method will convert the
    * date string to a Calendar instance.

    * @param obj   The ModelObject to pull the property from
    * @param name  Name of the desired property
    * @return      The array of Calendar instances of the property
    *
    * @throws NotLoadedException   If the named property does not exist on the ModelObject.
    * This does not distinguish between a property that simply has not been downloaded, or
    * a invalid property name (one not defined in the Teamcenter Business Model).
    */
   public static Calendar[] getPropertyDateValues( ModelObject obj, String name )
   throws NotLoadedException
   {
        String[] xsdDates = getPropertyStringValues( obj, name );
        Calendar[] cals = new Calendar[xsdDates.length];

        for (int i = 0; i < cals.length; i++)
        {
            String xsdDate = xsdDates[i];
            try
            {
                // SimpleDateFormate does not like the : in the time zone 2006-03-15T14:20:45-07:00
                // get rid of the last :
                int hhColon = xsdDate.indexOf(':');
                int mmColon = xsdDate.indexOf(':', hhColon+1);
                int zzColon = xsdDate.indexOf(':', mmColon+1);
                if(zzColon != -1)
                    xsdDate = xsdDate.substring(0, zzColon) + xsdDate.substring(zzColon+1);

                Calendar wireCal = new GregorianCalendar();
                wireCal.setTime(xsdFormat.parse( xsdDate ));
                cals[i]= toLocal( wireCal );
            }
            catch(ParseException e){ throw new IllegalArgumentException( e.getMessage());}

        }
        return cals;
    }


   /**
    * Return a double for the named property.
    * All properties are stored as plain strings on the ModelObject, properties that
    * represent doubles are store as xsd::double syntax. This method will convert the
    * double string to a double.

    * @param obj   The ModelObject to pull the property from
    * @param name  Name of the desired property
    * @return      The double value of the property
    *
    * @throws NotLoadedException   If the named property does not exist on the ModelObject.
    * This does not distinguish between a property that simply has not been downloaded, or
    * a invalid property name (one not defined in the Teamcenter Business Model).
    */
   public static double getPropertyDoubleValue( ModelObject obj, String name )
   throws NotLoadedException
   {
       double[] allDouble = getPropertyDoubleValues( obj, name );
       return allDouble[0];
   }


   /**
    * Return an array of double for the named property.
    * All properties are stored as plain strings on the ModelObject, properties that
    * represent doubles are store as xsd::double syntax. This method will convert the
    * double string to a double.

    * @param obj   The ModelObject to pull the property from
    * @param name  Name of the desired property
    * @return      The array of doubles values of the property
    *
    * @throws NotLoadedException   If the named property does not exist on the ModelObject.
    * This does not distinguish between a property that simply has not been downloaded, or
    * a invalid property name (one not defined in the Teamcenter Business Model).
    */
   public static double[] getPropertyDoubleValues( ModelObject obj, String name )
   throws NotLoadedException
   {
       String[] xsdDoubles = getPropertyStringValues( obj, name );
       double[] doubles = new double[xsdDoubles.length];


       for (int i = 0; i < doubles.length; i++)
       {
           if(xsdDoubles[i] == null || xsdDoubles[i].length() == 0)
               doubles[i] = 0.0;
           else
               doubles[i] = Double.parseDouble(xsdDoubles[i]);
       }

       return doubles;
   }


   /**
    * Return a float for the named property.
    * All properties are stored as plain strings on the ModelObject, properties that
    * represent float are store as xsd::float syntax. This method will convert the
    * float string to a float.

    * @param obj   The ModelObject to pull the property from
    * @param name  Name of the desired property
    * @return      The float value of the property
    *
    * @throws NotLoadedException   If the named property does not exist on the ModelObject.
    * This does not distinguish between a property that simply has not been downloaded, or
    * a invalid property name (one not defined in the Teamcenter Business Model).
    */
   public static float getPropertyFloatValue( ModelObject obj, String name )
   throws NotLoadedException
   {
       float[] allFloats = getPropertyFloatValues( obj, name );
       return allFloats[0];
   }


   /**
    * Return an array of floats for the named property.
    * All properties are stored as plain strings on the ModelObject, properties that
    * represent float are store as xsd::float syntax. This method will convert the
    * float string to a float.

    * @param obj   The ModelObject to pull the property from
    * @param name  Name of the desired property
    * @return      The array of floats values of the property
    *
    * @throws NotLoadedException   If the named property does not exist on the ModelObject.
    * This does not distinguish between a property that simply has not been downloaded, or
    * a invalid property name (one not defined in the Teamcenter Business Model).
    */
   public static float[] getPropertyFloatValues( ModelObject obj, String name )
   throws NotLoadedException
   {
       String[] xsdFloats = getPropertyStringValues( obj, name );
       float[] floats = new float[xsdFloats.length];


       for (int i = 0; i < floats.length; i++)
       {
           if(xsdFloats[i] == null || xsdFloats[i].length() == 0)
               floats[i] = 0.0f;
           else
               floats[i] = Float.parseFloat(xsdFloats[i]);
       }

       return floats;
   }

   /**
    * Return a int for the named property.
    * All properties are stored as plain strings on the ModelObject, properties that
    * represent int are store as xsd::integer syntax. This method will convert the
    * int string to a int.

    * @param obj   The ModelObject to pull the property from
    * @param name  Name of the desired property
    * @return      The int value of the property
    *
    * @throws NotLoadedException   If the named property does not exist on the ModelObject.
    * This does not distinguish between a property that simply has not been downloaded, or
    * a invalid property name (one not defined in the Teamcenter Business Model).
    */
   public static int getPropertyIntegerValue( ModelObject obj, String name )
   throws NotLoadedException
   {
       int[] allInts = getPropertyIntegerValues( obj, name );
       return allInts[0];
   }


   /**
    * Return an array of ints for the named property.
    * All properties are stored as plain strings on the ModelObject, properties that
    * represent ints are store as xsd::integer syntax. This method will convert the
    * int string to a int.

    * @param obj   The ModelObject to pull the property from
    * @param name  Name of the desired property
    * @return      The array of int values of the property
    *
    * @throws NotLoadedException   If the named property does not exist on the ModelObject.
    * This does not distinguish between a property that simply has not been downloaded, or
    * a invalid property name (one not defined in the Teamcenter Business Model).
    */
   public static int[] getPropertyIntegerValues( ModelObject obj, String name )
   throws NotLoadedException
   {
       String[] xsdInts = getPropertyStringValues( obj, name );
       int[] ints = new int[xsdInts.length];


       for (int i = 0; i < ints.length; i++)
       {
           if(xsdInts[i] == null || xsdInts[i].length() == 0)
               ints[i] = 0;
           else
               ints[i] = Integer.parseInt(xsdInts[i]);
       }

       return ints;
   }



   /**
    * Return a short for the named property.
    * All properties are stored as plain strings on the ModelObject, properties that
    * represent int are store as xsd::integer syntax. This method will convert the
    * int string to a short.

    * @param obj   The ModelObject to pull the property from
    * @param name  Name of the desired property
    * @return      The short value of the property
    *
    * @throws NotLoadedException   If the named property does not exist on the ModelObject.
    * This does not distinguish between a property that simply has not been downloaded, or
    * a invalid property name (one not defined in the Teamcenter Business Model).
    */
   public static short getPropertyShortValue( ModelObject obj, String name )
   throws NotLoadedException
   {
       short[] allShorts = getPropertyShortValues( obj, name );
       return allShorts[0];
   }


   /**
    * Return an array of shorts for the named property.
    * All properties are stored as plain strings on the ModelObject, properties that
    * represent ints are store as xsd::integer syntax. This method will convert the
    * int string to a short.

    * @param obj   The ModelObject to pull the property from
    * @param name  Name of the desired property
    * @return      The array of short values of the property
    *
    * @throws NotLoadedException   If the named property does not exist on the ModelObject.
    * This does not distinguish between a property that simply has not been downloaded, or
    * a invalid property name (one not defined in the Teamcenter Business Model).
    */
   public static short[] getPropertyShortValues( ModelObject obj, String name )
   throws NotLoadedException
   {
       String[] xsdShorts = getPropertyStringValues( obj, name );
       short[] shorts = new short[xsdShorts.length];


       for (int i = 0; i < shorts.length; i++)
       {
           if(xsdShorts[i] == null || xsdShorts[i].length() == 0)
               shorts[i] = 0;
           else
               shorts[i] = Short.parseShort(xsdShorts[i]);
       }

       return shorts;
   }

   /**
    * Return a boolean for the named property.
    * All properties are stored as plain strings on the ModelObject, properties that
    * represent boolean are store as xsd::boolean syntax. This method will convert the
    * boolean string to a boolean.

    * @param obj   The ModelObject to pull the property from
    * @param name  Name of the desired property
    * @return      The boolean value of the property
    *
    * @throws NotLoadedException   If the named property does not exist on the ModelObject.
    * This does not distinguish between a property that simply has not been downloaded, or
    * a invalid property name (one not defined in the Teamcenter Business Model).
    */
   public static boolean getPropertyBooleanValue( ModelObject obj, String name )
   throws NotLoadedException
   {
       boolean[] allBools = getPropertyBooleanValues( obj, name );
       return allBools[0];
   }



   /**
    * Return an array of booleans for the named property.
    * All properties are stored as plain strings on the ModelObject, properties that
    * represent booleans are store as xsd::boolean syntax. This method will convert the
    * boolean string to a boolean.

    * @param obj   The ModelObject to pull the property from
    * @param name  Name of the desired property
    * @return      The array of int values of the property
    *
    * @throws NotLoadedException   If the named property does not exist on the ModelObject.
    * This does not distinguish between a property that simply has not been downloaded, or
    * a invalid property name (one not defined in the Teamcenter Business Model).
    */
   public static boolean[] getPropertyBooleanValues( ModelObject obj, String name )
   throws NotLoadedException
   {
       String[] xsdBools = getPropertyStringValues( obj, name );
       boolean[] bools = new boolean[xsdBools.length];


       for (int i = 0; i < bools.length; i++)
       {
           if ( xsdBools[i] != null && (xsdBools[i].equals("1") || xsdBools[i].equals("true")) )
               bools[i] = true;
           else
               bools[i] = false;
       }

       return bools;
   }





    private static Calendar toLocal( Calendar wire )
    {
        String wireString = wireFormat.format(wire.getTime());
        if(wireString.equals(nullString))
            return null;
        return wire;
    }

}
