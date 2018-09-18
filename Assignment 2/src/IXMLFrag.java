import tester.Tester;

interface ILoXMLFrag {
  int contentLength();
  boolean hasTag(String name);
  boolean hasAttribute(String name);
  boolean hasAttributeInTag(String tagName, String attName);
  ILoXMLFrag updateAttribute(String name, String value);
}

class MtLoXMLFrag implements ILoXMLFrag {
  public int contentLength() {
    return 0;
  }

  //does object contain a tag with given name?
  public boolean hasTag(String name) {
    return false;
  }

  //does object contain an attribute with given name?
  public boolean hasAttribute(String name) {
    return false;
  }

  //does object contain an attribute with given name inside of a tag of a given name?
  public boolean hasAttributeInTag(String tagName, String attName) {
    return false;
  }

  //update all instances of attribute with given name to the given value
  public ILoXMLFrag updateAttribute(String name, String value) {
    return this;
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

  //how many total characters in the objects within this list?
  public int contentLength() {
    return this.first.contentLength() + this.rest.contentLength();
  }

  //does object contain a tag with given name?
  public boolean hasTag(String name) {
    return this.first.hasTag(name) || this.rest.hasTag(name);
  }

  //does object contain an attribute with given name?
  public boolean hasAttribute(String name) {
    return this.first.hasAttribute(name) || this.rest.hasAttribute(name);
  }

  //does object contain an attribute with given name inside of a tag of a given name?
  public boolean hasAttributeInTag(String tagName, String attName) {
    return this.first.hasAttributeInTag(tagName, attName) ||
            this.rest.hasAttributeInTag(tagName, attName);
  }


  public ILoXMLFrag updateAttribute(String name, String value) {
    return null;
  }
}





interface IXMLFrag {
  int contentLength();
  boolean hasTag(String name);
  boolean hasAttribute(String name);
  boolean hasAttributeInTag(String tagName, String attName);
}

class Plaintext implements IXMLFrag {
  String txt;

  Plaintext(String txt) {
    this.txt = txt;
  }

  public int contentLength() {
    return this.txt.length();
  }

  public boolean hasTag(String name) {
    return false;
  }

  public boolean hasAttribute(String name) {
    return false;
  }

  public boolean hasAttributeInTag(String tagName, String attName) {
    return false;
  }
}

class Tagged implements IXMLFrag {
  Tag tag;
  ILoXMLFrag content;

  Tagged(Tag tag, ILoXMLFrag content) {
    this.tag = tag;
    this.content = content;
  }

  public int contentLength() {
    return this.content.contentLength();
  }

  public boolean hasTag(String name) {
    return this.tag.hasTag(name) || this.content.hasTag(name);
  }

  public boolean hasAttribute(String name) {
    return this.tag.hasAttribute(name) || this.content.hasAttribute(name);
  }

  public boolean hasAttributeInTag(String tagName, String attName) {
    return this.tag.hasAttributeInTag(tagName, attName) || this.content.hasAttributeInTag(tagName, attName);
  }
}

class Tag {
  String name;
  ILoAtt atts;

  Tag(String name, ILoAtt atts) {
    this.name = name;
    this.atts = atts;
  }

  boolean hasTag(String name) {
    return this.name.equals(name);
  }

  boolean hasAttribute(String name) {
    return this.atts.hasAttribute(name);
  }

  boolean hasAttributeInTag(String tagName, String attName) {
    return this.name.equals(tagName) && this.atts.hasAttribute(attName);
  }
}




// List of Attributes implementation
interface ILoAtt {
  boolean hasAttribute(String name);
}

class ConsLoAtt implements ILoAtt {
  Att first;
  ILoAtt rest;

  ConsLoAtt(Att first, ILoAtt rest) {
    this.first = first;
    this.rest = rest;
  }

  public boolean hasAttribute(String name) {
    return this.first.hasAttribute(name) || this.rest.hasAttribute(name);
  }
}

class MtLoAtt implements ILoAtt {

  public boolean hasAttribute(String name) {
    return false;
  }
}


class Att {
  String name;
  String value;

  Att(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public boolean hasAttribute(String name) {
    return this.name.equals(name);
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

  ILoAtt loatt1 = new ConsLoAtt(att1, new ConsLoAtt(att2, new ConsLoAtt(att3, new MtLoAtt())));
  ILoAtt loatt2 = new ConsLoAtt(att4, new ConsLoAtt(att5, new ConsLoAtt(att6, new MtLoAtt())));

  Tag tag1 = new Tag("yell", loatt1);
  Tag tag2 = new Tag("smack", loatt2);
  IXMLFrag plaintext1 = new Plaintext("Alex Perez");  // length 10
  IXMLFrag plaintext2 = new Plaintext("bleep bloop"); // length 11

  ILoXMLFrag xml1 = new ConsLoXMLFrag(plaintext1, new MtLoXMLFrag());
  IXMLFrag tagged1 = new Tagged(tag1, xml1);

  ILoXMLFrag xml2 = new ConsLoXMLFrag(tagged1, new MtLoXMLFrag());
  IXMLFrag tagged2 = new Tagged(tag2, xml2);

  ILoXMLFrag xml3 = new ConsLoXMLFrag(tagged1,
          new ConsLoXMLFrag(plaintext1,
                  new ConsLoXMLFrag(plaintext2,
                          new ConsLoXMLFrag(tagged2,
                                  new MtLoXMLFrag()))));




  boolean testContentLength(Tester t) {
    return t.checkExpect(xml1.contentLength(), 10) &&
            t.checkExpect(xml2.contentLength(), 10) &&
            t.checkExpect(xml3.contentLength(), 41); // 10 + 10 + 11 + 10
  }

  boolean testHasTag(Tester t) {
    return t.checkExpect(xml1.hasTag("yell"), false) &&
            t.checkExpect(xml2.hasTag("yell"), true) &&
            t.checkExpect(xml3.hasTag("smack"), true) &&
            t.checkExpect(xml3.hasTag("whoop"), false);
  }

  boolean testHasAttribute(Tester t) {
    return t.checkExpect(xml1.hasAttribute("hangtime"), false) &&
            t.checkExpect(xml2.hasAttribute("volume"), true) &&
            t.checkExpect(xml2.hasAttribute("height"), false) &&
            t.checkExpect(xml3.hasAttribute("duration"), true) &&
            t.checkExpect(xml3.hasAttribute("force"), true) &&
            t.checkExpect(xml3.hasAttribute("boop"), false);
  }

  boolean testHasAttributeInTag(Tester t) {
    return t.checkExpect(xml1.hasAttributeInTag("yell", "volume"), false) &&
            t.checkExpect(xml2.hasAttributeInTag("smack", "force"), false) &&
            t.checkExpect(xml2.hasAttributeInTag("yell", "volume"), true) &&
            t.checkExpect(xml3.hasAttributeInTag("yell", "duration"), true) &&
            t.checkExpect(xml3.hasAttributeInTag("yell", "hangtime"), false);
  }
}