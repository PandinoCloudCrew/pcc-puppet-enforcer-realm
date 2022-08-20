/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.file;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FileResourceMapper {
  FileResourceMapper INSTANCE = Mappers.getMapper(FileResourceMapper.class);

  FileResource createCommandToFile(FileResourceCreateCommand fileResourceCreateCommand);
}
