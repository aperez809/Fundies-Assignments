import tester.Tester;

interface ILoXMLFrag {

  //is this ILoXMLFrag the same as other ILoXMLFrag
  boolean sameXMLDoc(ILoXMLFrag other);

  //is this ConsLoXMLFrag the same as other ConsLoXMLFrag
  boolean sameConsLoXMLFrag(ConsLoXMLFrag other);

  //is this MtLoXMLFrag the same as other  MtLoXMLFrag
  boolean sameMtLoXMLFrag(MtLoXMLFrag other);
}

class MtLoXMLFrag implements ILoXMLFrag {

  //is this ILoXMLFrag the same as other ILoXMLFrag
  public boolean sameXMLDoc(ILoXMLFrag other) {
    return other.sameMtLoXMLFrag(this);
  }

  //is this ConsLoXMLFrag the same as other ConsLoXMLFrag
  public boolean sameConsLoXMLFrag(ConsLoXMLFrag other) {
    return false;
  }

  //is this MtLoXMLFrag the same as other  MtLoXMLFrag
  public boolean sameMtLoXMLFrag(MtLoXMLFrag other) {
    return true;
  }
}

class ConsLoXMLFrag implements ILoXMLFrag {
  //Fields
  IXMLFrag first;
  ILoXMLFrag rest;

  //Constructor
  ConsLoXMLFrag(IXMLFrag first, ILoXMLFrag rest) {
    this.first = first;
    this.rest = rest;
  }

  /* Template:
  Fields:
  this.first ... IXMLFrag
  this.rest  ... ILoXMLFrag

  Methods:
  this.contentLength()                            ... int
  this.hasTag(String name)                        ... boolean
  this.hasAttribute(String name)                  ... boolean
  this.hasAttributeInTag(String name)             ... boolean
  this.updateAttribute(String name, String value) ... ILoXMLFrag
  this.renderAsString()                           ... String

  Methods of Fields:
  this.first.contentLength()                            ... int
  this.first.hasTag(String name)                        ... boolean
  this.first.hasAttribute(String name)                  ... boolean
  this.first.hasAttributeInTag(String name)             ... boolean
  this.first.updateAttribute(String name, String value) ... ILoXMLFrag
  this.first.renderAsString()                           ... String

  this.rest.contentLength()                            ... int
  this.rest.hasTag(String name)                        ... boolean
  this.rest.hasAttribute(String name)                  ... boolean
  this.rest.hasAttributeInTag(String name)             ... boolean
  this.rest.updateAttribute(String name, String value) ... ILoXMLFrag
  this.rest.renderAsString()                           ... String
   */

  //is this ILoXMLFrag the same as other ILoXMLFrag
  public boolean sameXMLDoc(ILoXMLFrag other) {
    return other.sameConsLoXMLFrag(this);
  }

  //is this ConsLoXMLFrag the same as other ConsLoXMLFrag
  public boolean sameConsLoXMLFrag(ConsLoXMLFrag other) {
    return this.first.sameXMLFragDD(other.first)
            && this.rest.sameXMLDoc(other.rest);
  }

  //is this MtLoXMLFrag the same as other  MtLoXMLFrag
  public boolean sameMtLoXMLFrag(MtLoXMLFrag other) {
    return false;
  }
}

interface IXMLFrag {

  //is this XMLFrag the same as other XMLFrag
  boolean sameXMLFrag(IXMLFrag other);

  boolean sameXMLFragDD(IXMLFrag other);

  //is this Plaintext the same as other Plaintext
  boolean samePlaintext(Plaintext other);

  //is this Tagged the same as other Tagged
  boolean sameTagged(Tagged other);
}

class Plaintext implements IXMLFrag {
  String txt;

  Plaintext(String txt) {
    this.txt = txt;
  }

  /* Template:
  Fields:
  this.txt ... String

  Methods:
  this.contentLength()                            ... int
  this.hasTag(String name)                        ... boolean
  this.hasAttribute(String name)                  ... boolean
  this.hasAttributeInTag(String name)             ... boolean
  this.updateAttribute(String name, String value) ... IXMLFrag
  this.renderAsString()                           ... String

  Methods of Fields:

   */

  //is this Plaintext the same as other Plaintext
  public boolean samePlaintext(Plaintext other) {
    return this.txt.equals(other.txt);
  }

  //is this Tagged the same as other Tagged
  public boolean sameTagged(Tagged other) {
    return false;
  }

  //is this XMLFrag the same as other XMLFrag (using casting)
  public boolean sameXMLFrag(IXMLFrag other) {
    if (other instanceof Plaintext) {
      Plaintext newOther = (Plaintext) other;
      return newOther.txt.equals(this.txt);
    }
    else {
      return false;
    }
  }

  //is this XMLFrag the same as other XMLFrag (using double dispatch)
  public boolean sameXMLFragDD(IXMLFrag other) {
    return other.samePlaintext(this);
  }
}

class Tagged implements IXMLFrag {
  Tag tag;
  ILoXMLFrag content;

  Tagged(Tag tag, ILoXMLFrag content) {
    this.tag = tag;
    this.content = content;
  }

  /* Template:
  Fields:
  this.tag ... Tag
  this.content  ... ILoXMLFrag

  Methods:
  this.contentLength()                            ... int
  this.hasTag(String name)                        ... boolean
  this.hasAttribute(String name)                  ... boolean
  this.hasAttributeInTag(String name)             ... boolean
  this.updateAttribute(String name, String value) ... IXMLFrag
  this.renderAsString()                           ... String

  Methods of Fields:
  this.tag.contentLength()                            ... int
  this.tag.hasTag(String name)                        ... boolean
  this.tag.hasAttribute(String name)                  ... boolean
  this.tag.hasAttributeInTag(String name)             ... boolean
  this.tag.updateAttribute(String name, String value) ... IXMLFrag
  this.tag.renderAsString()                           ... String

  this.content.contentLength()                            ... int
  this.content.hasTag(String name)                        ... boolean
  this.content.hasAttribute(String name)                  ... boolean
  this.content.hasAttributeInTag(String name)             ... boolean
  this.content.updateAttribute(String name, String value) ... ILoXMLFrag
  this.content.renderAsString()                           ... String
   */

  //is this XMLFrag the same as other XMLFrag
  public boolean sameXMLFragDD(IXMLFrag other) {
    return other.sameTagged(this);
  }

  //is this Plaintext the same as other Plaintext
  public boolean samePlaintext(Plaintext other) {
    return false;
  }

  //is this XMLFrag the same as other XMLFrag (using casting)
  public boolean sameXMLFrag(IXMLFrag other) {
    if (other instanceof Tagged) {
      Tagged newOther = (Tagged) other;
      return newOther.tag.sameTag(this.tag)
              && newOther.content.sameXMLDoc(this.content);
    }
    else {
      return false;
    }
  }

  //is this Tagged the same as other Tagged
  public boolean sameTagged(Tagged other) {
    return this.tag.sameTag(other.tag)
            && this.content.sameXMLDoc(other.content);
  }
}

class Tag {
  String name;
  ILoAtt atts;

  Tag(String name, ILoAtt atts) {
    this.name = name;
    this.atts = atts;
  }

  /* Template:
  Fields:
  this.name ... String
  this.att  ... ILoAtts

  Methods:
  this.hasTag(String name)                        ... boolean
  this.hasAttribute(String name)                  ... boolean
  this.hasAttributeInTag(String name)             ... boolean
  this.updateAttribute(String name, String value) ... Tag

  Methods of Fields:
  this.atts.hasAttribute(String name)                  ... boolean
  this.atts.updateAttribute(String name, String value) ... ILoAtt
   */

  //is this Tag the same as the other Tag
  public boolean sameTag(Tag other) {
    return this.name.equals(other.name)
            && this.atts.sameILoAtt(other.atts);
  }
}

// List of Attributes implementation
interface ILoAtt {

  //is this ILoAtt the same as the other ILoAtt
  boolean sameILoAtt(ILoAtt other);

  //is this ConsLoAtt the same as the other ConsLoAtt
  boolean sameConsLoAtt(ConsLoAtt other);

  //is this MtLoAtt the same as the other MtLoAtt
  boolean sameMtLoAtt(MtLoAtt other);
}

class ConsLoAtt implements ILoAtt {
  Att first;
  ILoAtt rest;

  ConsLoAtt(Att first, ILoAtt rest) {
    this.first = first;
    this.rest = rest;
  }

  /* Template:
  Fields:
  this.first ... Att
  this.rest  ... ILoAtt

  Methods:
  this.hasAttribute(String name)                  ... boolean
  this.updateAttribute(String name, String value) ... ILoAtt

  Methods of Fields:
  this.first.hasAttribute(String name)                  ... boolean
  this.first.updateAttribute(String name, String value) ... Att

  this.content.hasAttribute(String name)                  ... boolean
  this.content.updateAttribute(String name, String value) ... ILoAtt
   */


  //is this ILoAtt the same as the other ILoAtt
  public boolean sameILoAtt(ILoAtt other) {
    return other.sameConsLoAtt(this);
  }


  //is this ConsLoAtt the same as the other ConsLoAtt
  public boolean sameConsLoAtt(ConsLoAtt other) {
    return this.first.sameAttribute(other.first)
            && this.rest.sameILoAtt(other.rest);
  }

  //is this MtLoAtt the same as the other ConsMtLoAttLoAtt
  public boolean sameMtLoAtt(MtLoAtt other) {
    return false;
  }
}

class MtLoAtt implements ILoAtt {

  //is this ILoAtt the same as the other ILoAtt
  public boolean sameILoAtt(ILoAtt other) {
    return other.sameMtLoAtt(this);
  }

  //is this ConsLoAtt the same as the other ConsLoAtt
  public boolean sameConsLoAtt(ConsLoAtt other) {
    return false;
  }

  //is this MtLoAtt the same as the other ConsMtLoAttLoAtt
  public boolean sameMtLoAtt(MtLoAtt other) {
    return true;
  }
}


class Att {
  String name;
  String value;

  Att(String name, String value) {
    this.name = name;
    this.value = value;
  }

  /* Template:
  Fields:
  this.name ... String
  this.value  ... String

  Methods:
  this.hasAttribute(String name)                  ... boolean
  this.updateAttribute(String name, String value) ... Att

  Methods of Fields:

   */

  //is this Attribute the same as the other Attribute
  boolean sameAttribute(Att other) {
    return this.name.equals(other.name)
            && this.value.equals(other.value);
  }
}



//Examples of XML
class ExamplesXML {
  Att att1 = new Att("volume", "30db");
  Att att2 = new Att("duration", "5sec");
  Att att3 = new Att("release", "2sec");
  Att att4 = new Att("force", "500N");
  Att att5 = new Att("height", "6ft");
  Att att6 = new Att("hangtime", "2.6sec");
  Att att7 = new Att("volume", "somethingRidiculous");

  ILoAtt loatt1 = new ConsLoAtt(att1, new ConsLoAtt(att2, new ConsLoAtt(att3, new MtLoAtt())));
  ILoAtt loatt2 = new ConsLoAtt(att4, new ConsLoAtt(att5, new ConsLoAtt(att6, new MtLoAtt())));
  ILoAtt loatt3 = new ConsLoAtt(att1, new MtLoAtt());
  ILoAtt loatt4 = new ConsLoAtt(att1, new ConsLoAtt(att2, new MtLoAtt()));
  ILoAtt loatt5 = new ConsLoAtt(att7, new ConsLoAtt(att2, new MtLoAtt()));


  Tag tag1 = new Tag("yell", loatt1);
  Tag tag2 = new Tag("smack", loatt2);
  IXMLFrag plainText1 = new Plaintext("Alex Perez");  // length 10
  IXMLFrag plainText2 = new Plaintext("bleep bloop"); // length 11
  IXMLFrag plainText3 = new Plaintext("I am ");
  IXMLFrag plainText4 = new Plaintext("XML");
  IXMLFrag plainText5 = new Plaintext("!");
  IXMLFrag plainText6 = new Plaintext("XM");
  IXMLFrag plainText7 = new Plaintext("L");
  IXMLFrag plainText8 = new Plaintext("I am XML!");
  IXMLFrag plainText9 = new Plaintext("X");
  IXMLFrag plainText10 = new Plaintext("ML");


  Tag tag3 = new Tag("yell", new MtLoAtt());
  Tag tag4 = new Tag("italic", new MtLoAtt());
  Tag tag5 = new Tag("yell", loatt3);
  Tag tag6 = new Tag("yell", loatt4);
  Tag tag7 = new Tag("yell", loatt5);

  Tagged tagged3 = new Tagged(tag3, new ConsLoXMLFrag(
          new Tagged(tag4, new ConsLoXMLFrag(
                  plainText9, new MtLoXMLFrag())),
          new MtLoXMLFrag()));

  ILoXMLFrag xml1 = new ConsLoXMLFrag(plainText8, new MtLoXMLFrag());

  ILoXMLFrag xml2 = new ConsLoXMLFrag(plainText3,
          new ConsLoXMLFrag(new Tagged(tag3,
                  new ConsLoXMLFrag(plainText4, new MtLoXMLFrag())),
                  new ConsLoXMLFrag(plainText5, new MtLoXMLFrag())));

  ILoXMLFrag xml3 = new ConsLoXMLFrag(plainText3,
          new ConsLoXMLFrag(
                  new Tagged(tag3,
                          new ConsLoXMLFrag(
                                  new Tagged(tag4,
                                          new ConsLoXMLFrag(plainText9, new MtLoXMLFrag())),
                                  new ConsLoXMLFrag(plainText10, new MtLoXMLFrag()))),
                  new ConsLoXMLFrag(plainText5, new MtLoXMLFrag())));

  ILoXMLFrag xml4 = new ConsLoXMLFrag(plainText3,
          new ConsLoXMLFrag(
                  new Tagged(tag5,
                          new ConsLoXMLFrag(
                                  new Tagged(tag4,
                                          new ConsLoXMLFrag(plainText9, new MtLoXMLFrag())),
                                  new ConsLoXMLFrag(plainText10, new MtLoXMLFrag()))),
                  new ConsLoXMLFrag(plainText5, new MtLoXMLFrag())));

  ILoXMLFrag xml5 = new ConsLoXMLFrag(plainText3,
          new ConsLoXMLFrag(
                  new Tagged(tag6,
                          new ConsLoXMLFrag(
                                  new Tagged(tag4,
                                          new ConsLoXMLFrag(plainText9, new MtLoXMLFrag())),
                                  new ConsLoXMLFrag(plainText10, new MtLoXMLFrag()))),
                  new MtLoXMLFrag()));

  boolean testSameAttribute(Tester t) {
    return t.checkExpect(att7.sameAttribute(att7), true)
            && t.checkExpect(att6.sameAttribute(att3), false);
  }


  boolean testSameXMLDoc(Tester t) {
    return t.checkExpect(xml5.sameXMLDoc(xml5), true)
            && t.checkExpect(xml2.sameXMLDoc(xml4), false)
            && t.checkExpect(new MtLoXMLFrag().sameXMLDoc(new MtLoXMLFrag()), true);
  }

  boolean testSameConsLoXMLFrag(Tester t) {
    return t.checkExpect(new MtLoXMLFrag().sameConsLoXMLFrag((ConsLoXMLFrag) xml4), false)
            && t.checkExpect(xml3.sameConsLoXMLFrag((ConsLoXMLFrag) xml3), true)
            && t.checkExpect(xml1.sameConsLoXMLFrag((ConsLoXMLFrag) xml2), false);
  }

  boolean testSameMtLoXMLFrag(Tester t) {
    return t.checkExpect(new MtLoXMLFrag().sameMtLoXMLFrag(new MtLoXMLFrag()),
            true)
            && t.checkExpect(xml2.sameMtLoXMLFrag(new MtLoXMLFrag()), false);
  }
}