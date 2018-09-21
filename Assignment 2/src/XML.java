import tester.Tester;

interface ILoXMLFrag {

  // what is the length of the list's contents?
  int contentLength();

  // does a tag with this name exist in the list?
  boolean hasTag(String name);

  // does an attribute with this name exist in the list?
  boolean hasAttribute(String name);

  // does a given attribute exist in a given tag in this list?
  boolean hasAttributeInTag(String tagName, String attName);

  // produce a new list of XMLFrags with any attributes of the
  // given name set to the given value
  ILoXMLFrag updateAttribute(String name, String value);

  // produce a string composed of the plaintext contents of this list
  String renderAsString();

}

class MtLoXMLFrag implements ILoXMLFrag {

  // what is the length of the list's contents?
  public int contentLength() {
    return 0;
  }

  // does a tag with this name exist in the list?
  public boolean hasTag(String name) {
    return false;
  }

  // does an attribute with this name exist in the list?
  public boolean hasAttribute(String name) {
    return false;
  }

  // does the given attribute exist in the given tag in this list?
  public boolean hasAttributeInTag(String tagName, String attName) {
    return false;
  }

  // produce a new list of XMLFrags with any attributes of the
  // given name set to the given value
  public ILoXMLFrag updateAttribute(String name, String value) {
    return this;
  }

  // produce a string composed of the plaintext contents of this list
  public String renderAsString() {
    return "";
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



  // what is the length of the list's contents?
  public int contentLength() {
    return this.first.contentLength() + this.rest.contentLength();
  }

  // does a tag with this name exist in the list?
  public boolean hasTag(String name) {
    return this.first.hasTag(name)
            || this.rest.hasTag(name);
  }

  // does an attribute with this name exist in the list?
  public boolean hasAttribute(String name) {
    return this.first.hasAttribute(name)
            || this.rest.hasAttribute(name);
  }

  // does the given attribute exist in the given tag in this list?
  public boolean hasAttributeInTag(String tagName, String attName) {
    return this.first.hasAttributeInTag(tagName, attName)
            || this.rest.hasAttributeInTag(tagName, attName);
  }

  // produce a string composed of the plaintext contents of this list
  public String renderAsString() {
    if (this.rest.renderAsString().equals("")) {
      return this.first.renderAsString() + this.rest.renderAsString();
    }
    else {
      return this.first.renderAsString() + this.rest.renderAsString();
    }

  }

  // produce a new list of XMLFrags with any attributes of the
  // given name set to the given value
  public ILoXMLFrag updateAttribute(String name, String value) {
    return new ConsLoXMLFrag(this.first.updateAttribute(name, value),
            this.rest.updateAttribute(name, value));
  }
}

interface IXMLFrag {

  // what is the length of the XMLFrag's contents?
  int contentLength();

  // does a tag with this name exist in this XMLFrag?
  boolean hasTag(String name);

  // does an attribute with this name exist in this XMLFrag?
  boolean hasAttribute(String name);

  // does the given attribute exist in the given tag in this XMLFrag?
  boolean hasAttributeInTag(String tagName, String attName);

  // produce a string from the plaintext contents of this XMLFrag
  String renderAsString();

  // produce a new XMLFrag with any attributes of the
  // given name set to the given value
  IXMLFrag updateAttribute(String name, String value);
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


  // what is the length of the XMLFrag's contents?
  public int contentLength() {
    return this.txt.length();
  }

  // does a tag with this name exist in this XMLFrag?
  public boolean hasTag(String name) {
    return false;
  }

  // does an attribute with this name exist in this XMLFrag?
  public boolean hasAttribute(String name) {
    return false;
  }

  // does the given attribute exist in the given tag in this XMLFrag?
  public boolean hasAttributeInTag(String tagName, String attName) {
    return false;
  }

  // produce a string from the plaintext contents of this XMLFrag
  public String renderAsString() {
    return this.txt;
  }

  // produce a new XMLFrag with any attributes of the
  // given name set to the given value
  public IXMLFrag updateAttribute(String name, String value) {
    return this;
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


  // what is the length of the XMLFrag's contents?
  public int contentLength() {
    return this.content.contentLength();
  }

  // does a tag with this name exist in this XMLFrag?
  public boolean hasTag(String name) {
    return this.tag.hasTag(name)
            || this.content.hasTag(name);
  }

  // does an attribute with this name exist in this XMLFrag?
  public boolean hasAttribute(String name) {
    return this.tag.hasAttribute(name)
            || this.content.hasAttribute(name);
  }

  // does the given attribute exist in the given tag in this XMLFrag?
  public boolean hasAttributeInTag(String tagName, String attName) {
    return this.tag.hasAttributeInTag(tagName, attName)
            || this.content.hasAttributeInTag(tagName, attName);
  }

  // produce a string from the plaintext contents of this XMLFrag
  public String renderAsString() {
    return this.content.renderAsString();
  }

  // produce a new XMLFrag with any attributes of the
  // given name set to the given value
  public IXMLFrag updateAttribute(String name, String value) {
    return new Tagged(this.tag.updateAttribute(name, value),
            this.content.updateAttribute(name, value));
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

  // does the tag's name equal the given name?
  boolean hasTag(String name) {
    return this.name.equals(name);
  }

  // does the tag contain an attribute of the given name?
  boolean hasAttribute(String name) {
    return this.atts.hasAttribute(name);
  }

  // does the tag's name equal the given name and
  // contain an attribute of the given name?
  boolean hasAttributeInTag(String tagName, String attName) {
    return this.name.equals(tagName) && this.atts.hasAttribute(attName);
  }

  // produce a new Tag with the given attribute set to the given value
  Tag updateAttribute(String name, String value) {
    return new Tag(this.name, this.atts.updateAttribute(name, value));
  }
}




// List of Attributes implementation
interface ILoAtt {

  // does an attribute of this name exist in the list?
  boolean hasAttribute(String name);

  // produce a new ILoAtt with the given attribute set to the given value
  ILoAtt updateAttribute(String name, String value);
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

  // does an attribute of this name exist in the list?
  public boolean hasAttribute(String name) {
    return this.first.hasAttribute(name)
            || this.rest.hasAttribute(name);
  }

  // produce a new ILoAtt with the given attribute set to the given value
  public ILoAtt updateAttribute(String name, String value) {
    return new ConsLoAtt(this.first.updateAttribute(name, value),
            this.rest.updateAttribute(name, value));
  }
}

class MtLoAtt implements ILoAtt {

  // does an attribute of this name exist in the list?
  public boolean hasAttribute(String name) {
    return false;
  }

  // produce a new ILoAtt with the given attribute set to the given value
  public ILoAtt updateAttribute(String name, String value) {
    return this;
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



  // does the name of this attribute match the given name?
  public boolean hasAttribute(String name) {
    return this.name.equals(name);
  }

  // produce a new Att where the attribute of the given name is set to the given value
  // if it exists
  public Att updateAttribute(String name, String value) {
    if (this.hasAttribute(name)) {
      return new Att(name, value);
    }
    else {
      return this;
    }
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

  boolean testContentLength(Tester t) {
    return t.checkExpect(xml1.contentLength(), 9)
            && t.checkExpect(xml2.contentLength(), 9)
            && t.checkExpect(xml3.contentLength(), 9); // 10 + 10 + 11 + 10
  }

  boolean testHasTag(Tester t) {
    return t.checkExpect(xml1.hasTag("yell"), false)
            && t.checkExpect(xml2.hasTag("yell"), true)
            && t.checkExpect(xml3.hasTag("smack"), false)
            && t.checkExpect(xml3.hasTag("whoop"), false)
            && t.checkExpect(xml4.hasTag("italic"), true);
  }

  boolean testHasAttribute(Tester t) {
    return t.checkExpect(xml1.hasAttribute("hangtime"), false)
            && t.checkExpect(xml2.hasAttribute("volume"), false)
            && t.checkExpect(xml2.hasAttribute("height"), false)
            && t.checkExpect(xml3.hasAttribute("duration"), false)
            && t.checkExpect(xml3.hasAttribute("force"), false)
            && t.checkExpect(xml3.hasAttribute("boop"), false);
  }

  boolean testHasAttributeInTag(Tester t) {
    return t.checkExpect(xml1.hasAttributeInTag("yell", "volume"),
            false)
            && t.checkExpect(xml2.hasAttributeInTag("smack", "force"),
            false)
            && t.checkExpect(xml2.hasAttributeInTag("yell", "volume"),
            false)
            && t.checkExpect(xml3.hasAttributeInTag("yell", "duration"),
            false)
            && t.checkExpect(xml3.hasAttributeInTag("jump", "hangtime"),
            false)
            && t.checkExpect(xml4.hasAttributeInTag("yell", "volume"), true);
  }

  boolean testRenderAsString(Tester t) {
    return t.checkExpect(xml1.renderAsString(), "I am XML!")
            && t.checkExpect(xml2.renderAsString(),"I am XML!")
            && t.checkExpect(xml3.renderAsString(), "I am XML!");
  }

  boolean testUpdateAttribute(Tester t) {
    return t.checkExpect(xml2.updateAttribute("volume", "somethingRidiculous"), xml2)
            && t.checkExpect(xml5.updateAttribute("volume", "somethingRidiculous"),
            new ConsLoXMLFrag(plainText3,
                    new ConsLoXMLFrag(
                            new Tagged(tag7,
                                    new ConsLoXMLFrag(
                                            new Tagged(tag4,
                                                    new ConsLoXMLFrag(plainText9,
                                                            new MtLoXMLFrag())),
                                            new ConsLoXMLFrag(plainText10, new MtLoXMLFrag()))),
                            new MtLoXMLFrag())));
  }
}